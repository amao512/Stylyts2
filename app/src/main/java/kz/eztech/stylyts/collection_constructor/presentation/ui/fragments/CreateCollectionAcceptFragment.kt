package kz.eztech.stylyts.collection_constructor.presentation.ui.fragments

import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import id.zelory.compressor.Compressor
import id.zelory.compressor.constraint.quality
import kotlinx.android.synthetic.main.base_toolbar.*
import kotlinx.android.synthetic.main.fragment_create_collection_accept.*
import kotlinx.coroutines.*
import kz.eztech.stylyts.R
import kz.eztech.stylyts.StylytsApp
import kz.eztech.stylyts.global.domain.models.clothes.ClothesModel
import kz.eztech.stylyts.global.domain.models.outfits.OutfitCreateModel
import kz.eztech.stylyts.global.domain.models.posts.PostCreateModel
import kz.eztech.stylyts.global.domain.models.user.UserModel
import kz.eztech.stylyts.global.presentation.common.ui.MainActivity
import kz.eztech.stylyts.global.presentation.base.BaseFragment
import kz.eztech.stylyts.global.presentation.base.BaseView
import kz.eztech.stylyts.global.presentation.base.DialogChooserListener
import kz.eztech.stylyts.collection_constructor.presentation.contracts.CreateCollectionAcceptContract
import kz.eztech.stylyts.collection_constructor.presentation.ui.dialogs.CreateCollectionChooserDialog
import kz.eztech.stylyts.collection_constructor.presentation.ui.dialogs.TagChooserDialog
import kz.eztech.stylyts.global.presentation.collection.ui.CollectionDetailFragment
import kz.eztech.stylyts.profile.presentation.profile.ui.ProfileFragment
import kz.eztech.stylyts.collection_constructor.presentation.presenters.CreateCollectionAcceptPresenter
import kz.eztech.stylyts.utils.EMPTY_STRING
import kz.eztech.stylyts.utils.FileUtils
import kz.eztech.stylyts.utils.extensions.hide
import kz.eztech.stylyts.utils.extensions.loadImage
import kz.eztech.stylyts.utils.extensions.show
import java.io.File
import javax.inject.Inject

/**
 * Created by Ruslan Erdenoff on 25.12.2020.
 */
class CreateCollectionAcceptFragment : BaseFragment<MainActivity>(), View.OnClickListener,
    DialogChooserListener,
    CreateCollectionAcceptContract.View {

    @Inject lateinit var presenter: CreateCollectionAcceptPresenter
    private lateinit var chooserDialog: CreateCollectionChooserDialog

    private var currentModel: OutfitCreateModel? = null
    private var selectedClothes = ArrayList<ClothesModel>()
    private var selectedUsers = ArrayList<UserModel>()
    private var listOfChosenImages = ArrayList<String>()

    companion object {
        const val OUTFIT_MODE = 0
        const val POST_MODE = 1
        const val MODE_KEY = "mode_key"
        const val ID_KEY = "idKey"
        const val OUTFIT_MODEL_KEY = "outfitModel"
        const val PHOTO_BITMAP_KEY = "photoBitmap"
        const val PHOTO_URI_KEY = "photoUri"
        const val CHOSEN_PHOTOS_KEY = "chooser_photos"
        const val IS_CHOOSER_KEY = "isChooser"
        const val IS_UPDATING_KEY = "isUpdating"
        const val CLOTHES_KEY = "clothes"
        const val USERS_KEY = "users"
    }

    override fun getLayoutId(): Int = R.layout.fragment_create_collection_accept

    override fun getContractView(): BaseView = this

    override fun customizeActionBar() {
        with(include_toolbar_dialog_create_collection) {
            toolbar_title_text_view.show()
            toolbar_title_text_view.text =
                context.getString(R.string.create_collection_create_outfit)

            toolbar_left_corner_action_image_button.setBackgroundResource(R.drawable.ic_baseline_keyboard_arrow_left_24)
            toolbar_left_corner_action_image_button.setOnClickListener(this@CreateCollectionAcceptFragment)
            toolbar_left_corner_action_image_button.show()

            toolbar_right_text_text_view.show()
            toolbar_right_text_text_view.text = context.getString(R.string.create_collection_ready)
            toolbar_right_text_text_view.setOnClickListener(this@CreateCollectionAcceptFragment)
        }
    }

    override fun initializeDependency() {
        (activity?.application as StylytsApp).applicationComponent.inject(fragment = this)
    }

    override fun initializePresenter() {
        presenter.attach(view = this)
    }

    override fun initializeViewsData() {
        chooserDialog = CreateCollectionChooserDialog(
            isPublication = when (getModeFromArgs()) {
                OUTFIT_MODE -> false
                else -> true
            }
        ).apply {
            setChoiceListener(listener = this@CreateCollectionAcceptFragment)
        }
        selectedUsers = ArrayList()
    }

    override fun initializeViews() {
        when (getModeFromArgs()) {
            OUTFIT_MODE -> {
                frame_layout_dialog_create_collection_accept_choose_clothes.hide()
                fragment_create_collection_accept_divier_one.hide()
            }
            POST_MODE -> {
                frame_layout_dialog_create_collection_accept_choose_clothes.show()
                fragment_create_collection_accept_divier_one.show()
            }
        }

        processPhotoChooser()
        updateUserView()
    }

    override fun initializeListeners() {
        chooserDialog.setChoiceListener(listener = this)

        frame_layout_dialog_create_collection_accept_user_search.setOnClickListener(this)
        frame_layout_dialog_create_collection_accept_choose_clothes.setOnClickListener(this)
    }

    override fun processPostInitialization() {
        processPhotos()
    }

    override fun disposeRequests() {
        presenter.disposeRequests()
    }

    override fun displayMessage(msg: String) {}

    override fun isFragmentVisible(): Boolean = isVisible

    override fun displayProgress() {
        dialog_create_collection_accept_progress_bar.show()
        dialog_create_collection_accept_view.isClickable = false
        dialog_create_collection_accept_view.isFocusable = false
    }

    override fun hideProgress() {
        dialog_create_collection_accept_progress_bar.hide()
        dialog_create_collection_accept_view.isClickable = true
        dialog_create_collection_accept_view.isFocusable = true
    }

    override fun initializeArguments() {
        arguments?.let {
            if (it.containsKey(OUTFIT_MODEL_KEY)) {
                currentModel = it.getParcelable(OUTFIT_MODEL_KEY)
            }
            if (it.containsKey(CLOTHES_KEY)) {
                it.getParcelableArrayList<ClothesModel>(CLOTHES_KEY)?.let { list ->
                    selectedClothes = list
                }
            }
            if (it.containsKey(USERS_KEY)) {
                it.getParcelableArrayList<UserModel>(USERS_KEY)?.let { list ->
                    selectedUsers = list
                }
            }
            if (it.containsKey(CHOSEN_PHOTOS_KEY)) {
                it.getStringArrayList(CHOSEN_PHOTOS_KEY)?.let { list ->
                    listOfChosenImages = list
                }
            }
        }
    }

    override fun onChoice(
        v: View?,
        item: Any?
    ) {
        when (v?.id) {
            R.id.dialog_bottom_create_collection_chooser_common_line -> savePost(isHidden = false)
            R.id.dialog_bottom_create_collection_chooser_wardrobe -> savePost(isHidden = true)
            R.id.dialog_bottom_create_collection_chooser_create -> currentModel?.let { saveOutfit(it) }
            R.id.dialog_bottom_photo_chooser_photo_as_collection_buy -> onBuyOutfitClicked()
        }

        when (item) {
            is Bundle -> {
                selectedClothes.clear()
                selectedUsers.clear()

                item.getParcelableArrayList<ClothesModel>(CLOTHES_KEY)?.map {
                    selectedClothes.add(it)
                }
                item.getParcelableArrayList<UserModel>(USERS_KEY)?.map {
                    selectedUsers.add(it)
                }

                initializeViews()
            }
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.frame_layout_dialog_create_collection_accept_user_search -> {
                showPhotoChooserDialog(mode = TagChooserDialog.USERS_MODE)
            }
            R.id.toolbar_back_text_view -> findNavController().navigateUp()
            R.id.toolbar_right_text_text_view -> {
                currentModel?.text =
                    edit_text_view_dialog_create_collection_accept_sign.text.toString()
                currentModel?.title =
                    edit_text_view_dialog_create_collection_accept_sign.text.toString()
                chooserDialog.show(childFragmentManager, "ChooserDialog")
            }
            R.id.frame_layout_dialog_create_collection_accept_choose_clothes -> {
                showPhotoChooserDialog(mode = TagChooserDialog.CLOTHES_MODE)
            }
            R.id.toolbar_left_corner_action_image_button -> findNavController().navigateUp()
        }
    }

    override fun processSuccessSavingOutfit(outfitModel: OutfitCreateModel) {
        val bundle = Bundle()
        bundle.putInt(ProfileFragment.MODE_KEY, ProfileFragment.OUTFITS_MODE)

        findNavController().navigate(R.id.nav_profile, bundle)
    }

    override fun processSuccessSavingPost(postModel: PostCreateModel) {
        findNavController().navigate(R.id.nav_profile)
    }

    override fun processSuccessUpdatingPost(postModel: PostCreateModel) {
        val bundle = Bundle()

        bundle.putInt(CollectionDetailFragment.MODE_KEY, CollectionDetailFragment.POST_MODE)
        bundle.putInt(CollectionDetailFragment.ID_KEY, postModel.id)

        findNavController().navigate(
            R.id.action_createCollectionAcceptFragment_to_collectionDetailFragment,
            bundle
        )
    }

    override fun processSuccessSavingToCart() {
        findNavController().navigate(R.id.nav_ordering)
    }

    private fun processPhotos() {
        getPhotoUriFromArgs()?.loadImage(target = image_view_dialog_create_collection_accept)
        getBitmapFromArgs()?.loadImage(target = image_view_dialog_create_collection_accept)
    }

    private fun processPhotoChooser() {
        if (isPhotoChooser()) {
            if (selectedClothes.isNotEmpty()) {
                text_view_dialog_create_collection_items_count.text = getString(
                    R.string.clothes_count_text_format,
                    selectedClothes.count().toString()
                )
                text_view_dialog_create_collection_items_count.show()
            } else {
                text_view_dialog_create_collection_items_count.hide()
            }
        }
    }

    private fun updateUserView() {
        if (selectedUsers.isNotEmpty()) {
            text_view_dialog_create_collection_user_count.text = getString(
                R.string.users_count_text_format,
                selectedUsers.count().toString()
            )
            text_view_dialog_create_collection_user_count.show()
        } else {
            text_view_dialog_create_collection_user_count.hide()
        }
    }

    private fun showPhotoChooserDialog(mode: Int) {
        TagChooserDialog.getNewInstance(
            token = currentActivity.getTokenFromSharedPref(),
            chooserListener = this,
            clothesList = selectedClothes,
            usersList = selectedUsers,
            mode = getModeFromArgs()
        ).apply {
            setMode(mode = mode)
            setPhotoUri(uri = getPhotoUriFromArgs())
            setPhotoBitmap(bitmap = getBitmapFromArgs())
        }.show(childFragmentManager, EMPTY_STRING)
    }

    private fun savePost(isHidden: Boolean) {
        val bitmap = getBitmapFromArgs()
        val uri = getPhotoUriFromArgs()

        try {
            if (bitmap != null && uri == null) {
                preparePostForCreating(
                    file = FileUtils.createPngFileFromBitmap(requireContext(), bitmap),
                    isHidden = isHidden
                )
            }

            if (uri != null && bitmap == null) {
                uri.path?.let {
                    preparePostForCreating(
                        file = File(it),
                        isHidden = isHidden
                    )
                }
            }
        } catch (e: Exception) {
            Log.e("TAG", e.localizedMessage ?: EMPTY_STRING)
        }
    }

    private fun preparePostForCreating(
        file: File?,
        isHidden: Boolean
    ) = lifecycleScope.launch {
        val images = ArrayList<File>()

        listOfChosenImages.map { imageString ->
            if (!isUpdating()) {
                FileUtils.getUriFromString(imageString)?.path?.let {
                    images.add(
                        getCompressedImageFile(file = File(it))
                    )
                }
            }
        }

        file?.let {
            val model = if (!isUpdating()) {
                PostCreateModel(
                    description = edit_text_view_dialog_create_collection_accept_sign.text.toString(),
                    clothesList = selectedClothes,
                    userList = selectedUsers,
                    imageFile = getCompressedImageFile(file),
                    images = images,
                    hidden = isHidden
                )
            } else {
                PostCreateModel(
                    description = edit_text_view_dialog_create_collection_accept_sign.text.toString(),
                    clothesList = selectedClothes,
                    userList = selectedUsers,
                    hidden = isHidden
                )
            }

            createPost(postCreateModel = model)
        }
    }

    private suspend fun getCompressedImageFile(file: File): File {
        return Compressor.compress(requireContext(), file) {
            quality(quality = 80)
        }
    }

    private fun createPost(postCreateModel: PostCreateModel) {
        if (isUpdating()) {
            presenter.updatePost(
                postCreateModel = postCreateModel,
                postId = getIdFromArgs()
            )
        } else {
            presenter.createPost(postCreateModel = postCreateModel)
        }
    }

    private fun saveOutfit(item: OutfitCreateModel) {
        try {
            getBitmapFromArgs()?.let { bitmap ->
                val file = FileUtils.createPngFileFromBitmap(requireContext(), bitmap)

                file?.let {
                    createOutfit(outfitCreateModel = item, file = it)
                } ?: run {
                    errorLoadData()
                }
            } ?: run {
                errorLoadData()
            }
        } catch (e: Exception) {
            errorLoadData()
        }
    }

    private fun createOutfit(
        outfitCreateModel: OutfitCreateModel,
        file: File
    ) {
        if (isUpdating()) {
            presenter.updateOutfit(
                id = getIdFromArgs(),
                model = outfitCreateModel,
                data = file
            )
        } else {
            presenter.createOutfit(
                model = outfitCreateModel,
                data = file
            )
        }
    }

    private fun onBuyOutfitClicked() {
        currentModel?.let {
            presenter.saveToCart(outfitCreateModel = it)
        }
    }

    private fun errorLoadData() {
        hideProgress()
        displayMessage(msg = getString(R.string.collection_constructor_error_load_data))
    }

    private fun getModeFromArgs(): Int = arguments?.getInt(MODE_KEY) ?: OUTFIT_MODE

    private fun getIdFromArgs(): Int = arguments?.getInt(ID_KEY) ?: 0

    private fun getBitmapFromArgs(): Bitmap? = arguments?.getParcelable(PHOTO_BITMAP_KEY)

    private fun getPhotoUriFromArgs(): Uri? = arguments?.getParcelable(PHOTO_URI_KEY)

    private fun isPhotoChooser(): Boolean = arguments?.getBoolean(IS_CHOOSER_KEY) ?: false

    private fun isUpdating(): Boolean = arguments?.getBoolean(IS_UPDATING_KEY) ?: false
}