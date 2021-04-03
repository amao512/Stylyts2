package kz.eztech.stylyts.presentation.dialogs.profile

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.base_toolbar.view.*
import kotlinx.android.synthetic.main.dialog_edit_profile.*
import kz.eztech.stylyts.R
import kz.eztech.stylyts.StylytsApp
import kz.eztech.stylyts.domain.models.UserModel
import kz.eztech.stylyts.presentation.base.EditorListener
import kz.eztech.stylyts.presentation.contracts.profile.EditProfileContract
import kz.eztech.stylyts.presentation.interfaces.UniversalViewClickListener
import kz.eztech.stylyts.presentation.presenters.profile.EditProfilePresenter
import kz.eztech.stylyts.presentation.utils.EMPTY_STRING
import kz.eztech.stylyts.presentation.utils.FileUtils
import kz.eztech.stylyts.presentation.utils.extensions.displaySnackBar
import kz.eztech.stylyts.presentation.utils.extensions.getShortName
import kz.eztech.stylyts.presentation.utils.extensions.hide
import kz.eztech.stylyts.presentation.utils.extensions.show
import javax.inject.Inject


/**
 * Created by Ruslan Erdenoff on 03.03.2021.
 */
class EditProfileDialog(
    private val editorListener: EditorListener
) : DialogFragment(), EditProfileContract.View, View.OnClickListener, UniversalViewClickListener {

    @Inject lateinit var presenter: EditProfilePresenter

    private lateinit var galleryResultLaunch: ActivityResultLauncher<Intent>
    private lateinit var cameraResultLaunch: ActivityResultLauncher<Intent>

    companion object {
        const val TOKEN_ARGS_KEY = "token_args_key"

        fun getNewInstance(
            token: String?,
            editorListener: EditorListener
        ): EditProfileDialog {
            val editProfileDialog = EditProfileDialog(editorListener)
            val args = Bundle()

            args.putString(TOKEN_ARGS_KEY, token)
            editProfileDialog.arguments = args

            return editProfileDialog
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.dialog_edit_profile, container, false)

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)

        initializeDependency()
        initializePresenter()
        initializeArguments()
        initializeViewsData()
        initializeViews()
        initializeListeners()
        processPostInitialization()
        customizeActionBar()
    }

    override fun getTheme(): Int = R.style.FullScreenDialog

    override fun customizeActionBar() {
        with(include_toolbar_edit_profile) {
            toolbar_left_corner_action_image_button.hide()

            toolbar_back_text_view.show()
            toolbar_back_text_view.text = context.getString(R.string.toolbar_cancel)
            toolbar_back_text_view.setCompoundDrawables(null, null, null, null)
            toolbar_back_text_view.setOnClickListener(this@EditProfileDialog)

            toolbar_title_text_view.show()
            toolbar_title_text_view.text = context.getString(R.string.toolbar_title_edit_profile)

            toolbar_right_corner_action_image_button.hide()

            toolbar_right_text_text_view.show()
            toolbar_right_text_text_view.text = context.getString(R.string.toolbar_completed)
            toolbar_right_text_text_view.setTextColor(
                ContextCompat.getColor(context, R.color.app_light_orange)
            )
            toolbar_right_text_text_view.setOnClickListener(this@EditProfileDialog)
        }
    }

    override fun initializeDependency() {
        (requireContext().applicationContext as StylytsApp).applicationComponent.inject(this)
    }

    override fun initializePresenter() = presenter.attach(view = this)

    override fun initializeArguments() {}

    override fun initializeViewsData() {
        galleryResultLaunch = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                setProfileImage(uri = result.data?.data)
            }
        }

        cameraResultLaunch = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                setProfileImageByBitmap(bitmap = result.data?.extras?.get("data") as? Bitmap)
            }
        }
    }

    override fun initializeViews() {}

    override fun initializeListeners() {
        dialog_edit_profile_change_photo_text_view.setOnClickListener(this)
    }

    override fun processPostInitialization() {
        presenter.getProfile(token = getTokenFromArguments())
    }

    override fun disposeRequests() {}

    override fun displayMessage(msg: String) {
        view?.let {
            displaySnackBar(
                context = requireContext(),
                view = it,
                msg = msg
            )
        }
    }

    override fun isFragmentVisible(): Boolean = isVisible

    override fun displayProgress() {}

    override fun hideProgress() {}

    override fun processUserModel(userModel: UserModel) {
        userModel.let { user ->
            edit_text_dialog_edit_profile_name.setText("${user.firstName} ${user.lastName}")
            edit_text_dialog_edit_profile_username.setText(user.username)
            edit_text_dialog_edit_profile_insta.setText(user.instagram)
            edit_text_dialog_edit_profile_site.setText(user.webSite)

            user.avatar?.let {
                text_view_fragment_profile_edit_user_short_name.hide()

                Glide.with(this)
                    .load(it)
                    .centerCrop()
                    .into(shapeable_image_view_fragment_profile_avatar)
            } ?: run {
                text_view_fragment_profile_edit_user_short_name.text = getShortName(
                    firstName = user.firstName,
                    lastName = user.lastName
                )
            }
        }
    }

    override fun successEditing() {
        editorListener.completeEditing(isSuccess = true)
        dismiss()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.toolbar_back_text_view -> dismiss()
            R.id.toolbar_right_text_text_view -> completeEditing()
            R.id.dialog_edit_profile_change_photo_text_view -> {
                ChangeProfilePhotoDialog(universalViewClickListener = this)
                    .show(childFragmentManager, EMPTY_STRING)
            }
        }
    }

    override fun onViewClicked(
        view: View,
        position: Int,
        item: Any?
    ) {
        when (view.id) {
            R.id.dialog_bottom_change_profile_photo_choose_from_gallery_text_view -> openGalleryForImage()
            R.id.dialog_bottom_change_profile_photo_take_photo_text_view -> openCameraForImage()
            R.id.dialog_bottom_change_profile_photo_delete_text_view -> presenter.deleteProfilePhoto(
                token = getTokenFromArguments()
            )
        }
    }

    private fun completeEditing() {
        val data = HashMap<String, Any>()
        val names: List<String> = edit_text_dialog_edit_profile_name.text.split(" ")
        val webSite: String = edit_text_dialog_edit_profile_site.text.toString()
        val instagram: String = edit_text_dialog_edit_profile_insta.text.toString()

        if (names.size == 2) {
            if (names[0].isNotBlank()) {
                data["first_name"] = names[0]
            } else {
                displayMessage(msg = getString(R.string.fill_names_throught_space))

                return
            }
            if (names[1].isNotBlank()) {
                data["last_name"] = names[1]
            } else {
                displayMessage(msg = getString(R.string.fill_names_throught_space))

                return
            }
        } else {
            displayMessage(msg = getString(R.string.fill_names_throught_space))

            return
        }

        data["web_site"] = when (webSite.isBlank()) {
            true -> EMPTY_STRING
            false -> webSite
        }
        data["instagram"] = when (instagram.isBlank()) {
            true -> EMPTY_STRING
            false -> instagram
        }

        presenter.editProfile(
            token = getTokenFromArguments(),
            data = data
        )
    }

    private fun openGalleryForImage() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        galleryResultLaunch.launch(intent)
    }

    private fun openCameraForImage() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

        try {
            cameraResultLaunch.launch(intent)
        } catch (e: Exception) {}
    }

    private fun setProfileImage(uri: Uri?) {
        val bitmap = FileUtils.createBitmapFromUri(
            context = requireContext(),
            uri = uri
        )

        setProfileImageByBitmap(bitmap)
    }

    private fun setProfileImageByBitmap(bitmap: Bitmap?) {
        bitmap?.let {
            val file = FileUtils.createPngFileFromBitmap(requireContext(), it)

            file?.let {
                presenter.changeProfilePhoto(
                    token = getTokenFromArguments(),
                    file = file
                )
                text_view_fragment_profile_edit_user_short_name.text = EMPTY_STRING
                text_view_fragment_profile_edit_user_short_name.show()
            }
        }
    }

    private fun getTokenFromArguments(): String = arguments?.getString(TOKEN_ARGS_KEY) ?: EMPTY_STRING
}