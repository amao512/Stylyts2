package kz.eztech.stylyts.presentation.fragments.collection_constructor

import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.base_toolbar.*
import kotlinx.android.synthetic.main.fragment_create_collection_accept.*
import kz.eztech.stylyts.R
import kz.eztech.stylyts.StylytsApp
import kz.eztech.stylyts.data.models.SharedConstants
import kz.eztech.stylyts.domain.models.clothes.ClothesModel
import kz.eztech.stylyts.domain.models.outfits.OutfitCreateModel
import kz.eztech.stylyts.domain.models.outfits.OutfitModel
import kz.eztech.stylyts.domain.models.posts.PostCreateModel
import kz.eztech.stylyts.domain.models.posts.PostModel
import kz.eztech.stylyts.domain.models.user.UserModel
import kz.eztech.stylyts.presentation.activity.MainActivity
import kz.eztech.stylyts.presentation.base.BaseFragment
import kz.eztech.stylyts.presentation.base.BaseView
import kz.eztech.stylyts.presentation.base.DialogChooserListener
import kz.eztech.stylyts.presentation.contracts.collection_constructor.CreateCollectionAcceptContract
import kz.eztech.stylyts.presentation.dialogs.collection_constructor.CreateCollectionChooserDialog
import kz.eztech.stylyts.presentation.dialogs.collection_constructor.PhotoChooserDialog
import kz.eztech.stylyts.presentation.presenters.collection_constructor.CreateCollectionAcceptPresenter
import kz.eztech.stylyts.presentation.utils.EMPTY_STRING
import kz.eztech.stylyts.presentation.utils.FileUtils
import kz.eztech.stylyts.presentation.utils.extensions.hide
import kz.eztech.stylyts.presentation.utils.extensions.show
import java.io.File
import javax.inject.Inject

/**
 * Created by Ruslan Erdenoff on 25.12.2020.
 */
class CreateCollectionAcceptFragment : BaseFragment<MainActivity>(), View.OnClickListener,
    DialogChooserListener,
    CreateCollectionAcceptContract.View {

    @Inject
    lateinit var presenter: CreateCollectionAcceptPresenter

    private lateinit var chooserDialog: CreateCollectionChooserDialog

    private var currentMode: Int = OUTFIT_MODE
    private var currentModel: OutfitCreateModel? = null
    private var currentBitmap: Bitmap? = null
    private var currentPhotoUri: Uri? = null
    private var isPhotoChooser = false
    private var selectedList = ArrayList<ClothesModel>()
    private var selectedUsers = ArrayList<UserModel>()
    private var listOfChosenImages = ArrayList<String>()

    companion object {
        const val OUTFIT_MODE = 0
        const val POST_MODE = 1
        const val MODE_KEY = "mode_key"
        const val OUTFIT_MODEL_KEY = "outfitModel"
        const val PHOTO_BITMAP_KEY = "photoBitmap"
        const val PHOTO_URI_KEY = "photoUri"
        const val CHOSEN_PHOTOS_KEY = "chooser_photos"
        const val IS_CHOOSER_KEY = "isChooser"
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
        chooserDialog = CreateCollectionChooserDialog().apply {
            setChoiceListener(listener = this@CreateCollectionAcceptFragment)
        }
        selectedUsers = ArrayList()
    }

    override fun initializeViews() {
        when (currentMode) {
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
        chooserDialog.setChoiceListener(this)

        frame_layout_dialog_create_collection_accept_user_search.setOnClickListener(this)
        frame_layout_dialog_create_collection_accept_choose_clothes.setOnClickListener(this)
    }

    override fun processPostInitialization() {
        processPhotos()
    }

    override fun disposeRequests() {}

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
            if (it.containsKey(PHOTO_BITMAP_KEY)) {
                currentBitmap = it.getParcelable(PHOTO_BITMAP_KEY)
            }
            if (it.containsKey(PHOTO_URI_KEY)) {
                currentPhotoUri = it.getParcelable(PHOTO_URI_KEY)
            }
            if (it.containsKey(IS_CHOOSER_KEY)) {
                isPhotoChooser = it.getBoolean(IS_CHOOSER_KEY)
            }
            if (it.containsKey(CLOTHES_KEY)) {
                it.getParcelableArrayList<ClothesModel>(CLOTHES_KEY)?.let { list ->
                    selectedList = list
                }
            }
            if (it.containsKey(MODE_KEY)) {
                currentMode = it.getInt(MODE_KEY)
            }
            if (it.containsKey(CHOSEN_PHOTOS_KEY)) {
                it.getStringArrayList(CHOSEN_PHOTOS_KEY)?.let { list ->
                    listOfChosenImages = list
                }
            }
        }
    }

    override fun onChoice(v: View?, item: Any?) {
        when (item) {
            is Int -> {
                when (currentMode) {
                    OUTFIT_MODE -> currentModel?.let {
                        saveOutfit(it)
                    }
                    POST_MODE -> savePost()
                }
            }
            is Bundle -> {
                selectedList.clear()
                selectedUsers.clear()

                item.getParcelableArrayList<ClothesModel>(CLOTHES_KEY)?.map {
                    selectedList.add(it)
                }
                item.getParcelableArrayList<UserModel>(USERS_KEY)?.map {
                    selectedUsers.add(it)
                }
                currentPhotoUri = item.getParcelable(PHOTO_URI_KEY)
                isPhotoChooser = item.getBoolean(IS_CHOOSER_KEY)
                currentMode = item.getInt(MODE_KEY)

                initializeViews()
            }
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.frame_layout_dialog_create_collection_accept_user_search -> {
                showPhotoChooserDialog(mode = PhotoChooserDialog.USERS_MODE)
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
                showPhotoChooserDialog(mode = PhotoChooserDialog.CLOTHES_MODE)
            }
            R.id.toolbar_left_corner_action_image_button -> findNavController().navigateUp()
        }
    }

    override fun processSuccessPost(postModel: PostModel) {
        Log.d("TAG3", "success - $postModel")
        findNavController().navigateUp()
    }

    override fun processSuccessSaving(outfitModel: OutfitModel?) {
        displayMessage(msg = getString(R.string.collection_constructor_success_added))
        findNavController().navigateUp()
    }

    private fun processPhotos() {
        currentPhotoUri?.let {
            Glide.with(image_view_dialog_create_collection_accept.context)
                .load(it)
                .into(image_view_dialog_create_collection_accept)
        }

        currentBitmap?.let {
            Glide.with(image_view_dialog_create_collection_accept.context)
                .load(it)
                .into(image_view_dialog_create_collection_accept)
        }
    }

    private fun processPhotoChooser() {
        if (isPhotoChooser) {
            if (selectedList.isNotEmpty()) {
                text_view_dialog_create_collection_items_count.text = getString(
                    R.string.clothes_count_text_format,
                    selectedList.count().toString()
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
        PhotoChooserDialog.getNewInstance(
            token = getTokenFromSharedPref(),
            chooserListener = this,
            clothesList = selectedList,
            usersList = selectedUsers,
            mode = currentMode
        ).apply {
            setMode(mode = mode)
            setPhotoUri(uri = currentPhotoUri)
            setPhotoBitmap(bitmap = currentBitmap)
        }.show(childFragmentManager, EMPTY_STRING)
    }

    private fun savePost() {
        try {
            if (currentBitmap != null && currentPhotoUri == null) {
                currentBitmap?.let {
                    createPost(
                        FileUtils.createPngFileFromBitmap(requireContext(), it)
                    )
                }
            }

            if (currentPhotoUri != null && currentBitmap == null) {
                currentPhotoUri?.path?.let {
                    createPost(File(it))
                }
            }
        } catch (e: Exception) {
            Log.e("TAG", e.localizedMessage ?: EMPTY_STRING)
        }
    }

    private fun createPost(file: File?) {
        file?.let {
            val images = ArrayList<File>()

            listOfChosenImages.map {
                FileUtils.getUriFromString(it)?.path?.let { file ->
                    images.add(File(file))
                }
            }

            val model = PostCreateModel(
                description = edit_text_view_dialog_create_collection_accept_sign.text.toString(),
                clothesList = selectedList,
                userList = selectedUsers,
                imageFile = file,
                images = images
            )

            presenter.createPost(
                token = getTokenFromSharedPref(),
                postCreateModel = model
            )
        }
    }

    private fun saveOutfit(item: OutfitCreateModel) {
        getTokenFromSharedPref().let {
            try {
                currentBitmap?.let { bitmap ->
                    val file = FileUtils.createPngFileFromBitmap(requireContext(), bitmap)

                    file?.let { _ ->
                        presenter.saveCollection(
                            it, item, file
                        )
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
    }

    private fun errorLoadData() {
        hideProgress()
        displayMessage(msg = getString(R.string.collection_constructor_error_load_data))
    }

    private fun getTokenFromSharedPref(): String {
        return currentActivity.getSharedPrefByKey(SharedConstants.ACCESS_TOKEN_KEY) ?: EMPTY_STRING
    }
}