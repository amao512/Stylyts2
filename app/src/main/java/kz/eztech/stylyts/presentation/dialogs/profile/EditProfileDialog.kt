package kz.eztech.stylyts.presentation.dialogs.profile

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import kotlinx.android.synthetic.main.base_toolbar.view.*
import kotlinx.android.synthetic.main.dialog_edit_profile.*
import kz.eztech.stylyts.R
import kz.eztech.stylyts.StylytsApp
import kz.eztech.stylyts.domain.helpers.DomainImageLoader
import kz.eztech.stylyts.domain.models.user.UserModel
import kz.eztech.stylyts.presentation.activity.MainActivity
import kz.eztech.stylyts.presentation.base.EditorListener
import kz.eztech.stylyts.presentation.contracts.profile.EditProfileContract
import kz.eztech.stylyts.presentation.interfaces.UniversalViewClickListener
import kz.eztech.stylyts.presentation.presenters.profile.EditProfilePresenter
import kz.eztech.stylyts.presentation.utils.EMPTY_STRING
import kz.eztech.stylyts.presentation.utils.FileUtils
import kz.eztech.stylyts.presentation.utils.extensions.*
import javax.inject.Inject


/**
 * Created by Ruslan Erdenoff on 03.03.2021.
 */
class EditProfileDialog(
    private val editorListener: EditorListener
) : DialogFragment(), EditProfileContract.View, View.OnClickListener, UniversalViewClickListener {

    @Inject
    lateinit var presenter: EditProfilePresenter
    @Inject
    lateinit var imageLoader: DomainImageLoader

    private lateinit var galleryResultLaunch: ActivityResultLauncher<Intent>
    private lateinit var cameraResultLaunch: ActivityResultLauncher<Intent>

    private val permissions = arrayOf(
        Manifest.permission.CAMERA,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE
    )

    companion object {
        private const val TAG = "TAG"
        private const val REQUEST_CODE_PERMISSIONS = 10
        private const val FULL_NAME_TEXT_FORMAT = "%s %s"

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
        galleryResultLaunch =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == RESULT_OK) {
                    setProfileImage(uri = result.data?.data)
                }
            }

        cameraResultLaunch =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
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
        edit_text_dialog_edit_profile_name.setText(
            FULL_NAME_TEXT_FORMAT.format(userModel.firstName, userModel.lastName)
        )
        edit_text_dialog_edit_profile_username.setText(userModel.username)
        edit_text_dialog_edit_profile_insta.setText(userModel.instagram)
        edit_text_dialog_edit_profile_site.setText(userModel.webSite)

        if (userModel.avatar.isBlank()) {
            text_view_fragment_profile_edit_user_short_name.text = getShortName(
                firstName = userModel.firstName,
                lastName = userModel.lastName
            )
        } else {
            text_view_fragment_profile_edit_user_short_name.hide()
            imageLoader.load(
                url = userModel.avatar,
                target = shapeable_image_view_fragment_profile_avatar
            )

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

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                startCamera()
            } else {
                displayToast(context, msg = "Permissions not granted by the user.")
                dismiss()
            }
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
                displayMessage(msg = getString(R.string.edit_profile_fill_names_through_space))

                return
            }
            if (names[1].isNotBlank()) {
                data["last_name"] = names[1]
            } else {
                displayMessage(msg = getString(R.string.edit_profile_fill_names_through_space))

                return
            }
        } else {
            displayMessage(msg = getString(R.string.edit_profile_fill_names_through_space))

            return
        }

        data["web_site"] = checkStringValidation(text = webSite)
        data["instagram"] = checkStringValidation(text = instagram)

        presenter.editProfile(
            token = getTokenFromArguments(),
            data = data
        )
    }

    private fun checkStringValidation(text: String): String = when (text.isBlank()) {
        true -> EMPTY_STRING
        false -> text
    }

    private fun openGalleryForImage() {
        galleryResultLaunch.launch(
            Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        )
    }

    private fun openCameraForImage() {
        if (allPermissionsGranted()) {
            startCamera()
        } else {
            ActivityCompat.requestPermissions(
                activity as MainActivity,
                permissions,
                REQUEST_CODE_PERMISSIONS
            )
        }
    }

    private fun startCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

        try {
            cameraResultLaunch.launch(intent)
        } catch (e: Exception) {
            Log.wtf(TAG, e)
        }
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
            try {
                val file = FileUtils.createPngFileFromBitmap(requireContext(), it)

                file?.let {
                    presenter.changeProfilePhoto(
                        token = getTokenFromArguments(),
                        file = file
                    )
                    text_view_fragment_profile_edit_user_short_name.text = EMPTY_STRING
                    text_view_fragment_profile_edit_user_short_name.show()
                }
            } catch (e: Exception) {
                Log.wtf(TAG, e)
            }
        }
    }

    private fun getTokenFromArguments(): String =
        arguments?.getString(TOKEN_ARGS_KEY) ?: EMPTY_STRING

    private fun allPermissionsGranted() = permissions.all {
        ContextCompat.checkSelfPermission(
            activity as MainActivity,
            it
        ) == PackageManager.PERMISSION_GRANTED
    }
}