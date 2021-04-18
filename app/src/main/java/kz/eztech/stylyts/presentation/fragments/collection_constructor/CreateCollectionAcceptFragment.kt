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
import kz.eztech.stylyts.domain.models.CollectionPostCreateModel
import kz.eztech.stylyts.domain.models.PublicationModel
import kz.eztech.stylyts.domain.models.clothes.ClothesModel
import kz.eztech.stylyts.domain.models.user.UserModel
import kz.eztech.stylyts.presentation.activity.MainActivity
import kz.eztech.stylyts.presentation.base.BaseFragment
import kz.eztech.stylyts.presentation.base.BaseView
import kz.eztech.stylyts.presentation.base.DialogChooserListener
import kz.eztech.stylyts.presentation.contracts.collection_constructor.CreateCollectionAcceptContract
import kz.eztech.stylyts.presentation.dialogs.collection_constructor.CreateCollectionChooserDialog
import kz.eztech.stylyts.presentation.dialogs.collection_constructor.PhotoChooserDialog
import kz.eztech.stylyts.presentation.dialogs.collection_constructor.UserSearchDialog
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
    private lateinit var userSearchDialog: UserSearchDialog

    private var currentModel: CollectionPostCreateModel? = null
    private var currentBitmap: Bitmap? = null
    private var currentPhotoUri: Uri? = null
    private var isPhotoChooser = false
    private var selectedList: List<ClothesModel>? = null
    private var selectedUsers: ArrayList<UserModel>? = null
    private var hashMap = HashMap<String, Any>()

    companion object {
        const val COLLECTION_MODEL_KEY = "collectionModel"
        const val PHOTO_BITMAP_KEY = "photoBitmap"
        const val PHOTO_URI_KEY = "photoUri"
        const val IS_CHOOSER_KEY = "isChooser"
        const val CLOTHES_KEY = "clothes"
    }

    override fun getLayoutId(): Int = R.layout.fragment_create_collection_accept

    override fun getContractView(): BaseView = this

    override fun customizeActionBar() {
        with(include_toolbar_dialog_create_collection) {
            toolbar_back_text_view.show()
            toolbar_back_text_view.setOnClickListener(this@CreateCollectionAcceptFragment)

            toolbar_title_text_view.show()
            toolbar_title_text_view.text = context.getString(R.string.create_collection_create_outfit)

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
        userSearchDialog = UserSearchDialog.getNewInstance(
            token = getTokenFromSharedPref(),
            chooserListener = this
        )
    }

    override fun initializeViews() {}

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
            if (it.containsKey(COLLECTION_MODEL_KEY)) {
                currentModel = it.getParcelable(COLLECTION_MODEL_KEY)
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
                selectedList = it.getParcelableArrayList(CLOTHES_KEY)
            }
        }
    }

    override fun onChoice(v: View?, item: Any?) {
        when (item) {
            is Int -> {
//                currentModel?.let {
//                    hashMap["model"] = it
//                    when (item) {
//                        1 -> hashMap["mode"] = 1
//                        2 -> hashMap["mode"] = 2
//                        3 -> hashMap["mode"] = 3
//                    }
//
//                    listener?.onChoice(
//                        v = include_toolbar_dialog_create_collection.toolbar_right_text_text_view,
//                        item = hashMap
//                    )
//                    findNavController().navigateUp()
//                }
            }
            is UserModel -> selectedUsers?.let {
                if (!it.contains(item)) it.add(item)

                PhotoChooserDialog.getNewInstance(
                    token = getTokenFromSharedPref(),
                    photoUri = currentPhotoUri,
                    chooserListener = this,
                    mode = PhotoChooserDialog.USERS_MODE
                ).show(childFragmentManager, EMPTY_STRING)

                updateUserView()
            }
            is Bundle -> {
                selectedList = item.getParcelableArrayList(CLOTHES_KEY)
                currentPhotoUri = item.getParcelable(PHOTO_URI_KEY)
                isPhotoChooser = item.getBoolean(IS_CHOOSER_KEY)

                processPhotoChooser()
            }
        }

        when (v?.id) {
            R.id.dialog_bottom_create_collection_chooser_common_line -> onChoicePublication()
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.frame_layout_dialog_create_collection_accept_user_search -> {
                userSearchDialog.show(childFragmentManager, "UserSearchDialog")
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
                PhotoChooserDialog.getNewInstance(
                    token = getTokenFromSharedPref(),
                    photoUri = currentPhotoUri,
                    chooserListener = this
                ).show(childFragmentManager, EMPTY_STRING)
            }
        }
    }

    override fun processPublications(publicationModel: PublicationModel) {
        findNavController().popBackStack(R.id.profileFragment, false)
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
            frame_layout_dialog_create_collection_accept_choose_clothes.show()
            frame_layout_dialog_create_collection_accept_choose_clothes.setOnClickListener(this)

            selectedList?.let {
                text_view_dialog_create_collection_items_count.text = "${it.count()} вещ."
            }
        }
    }

    private fun updateUserView() {
        text_view_dialog_create_collection_user_count.show()

        selectedUsers?.let {
            if (it.isNotEmpty())
                text_view_dialog_create_collection_user_count.text = "${it.count()} юзер."
        }
    }

    private fun onChoicePublication() {
        try {
            if (currentBitmap != null && currentPhotoUri == null) {
                currentBitmap?.let {
                    val file: File? = FileUtils.createPngFileFromBitmap(requireContext(), it)

                    createPost(file)
                }
            }

            if (currentPhotoUri != null && currentBitmap == null) {
                currentPhotoUri?.let {
                    val file = File(it.path)

                    createPost(file)
                }
            }
        } catch (e: Exception) {
            Log.e("TAG", e.localizedMessage)
        }
    }

    private fun createPost(file: File?) {
        file?.let {
            presenter.createPublications(
                token = getTokenFromSharedPref(),
                description = edit_text_view_dialog_create_collection_accept_sign.text.toString(),
                tags = EMPTY_STRING,
                hidden = false,
                file = file
            )
        }
    }

    private fun getTokenFromSharedPref(): String {
        return currentActivity.getSharedPrefByKey(SharedConstants.ACCESS_TOKEN_KEY) ?: EMPTY_STRING
    }
}