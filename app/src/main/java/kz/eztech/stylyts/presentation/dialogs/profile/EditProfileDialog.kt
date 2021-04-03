package kz.eztech.stylyts.presentation.dialogs.profile

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.base_toolbar.view.*
import kotlinx.android.synthetic.main.dialog_edit_profile.*
import kz.eztech.stylyts.R
import kz.eztech.stylyts.StylytsApp
import kz.eztech.stylyts.presentation.base.EditorListener
import kz.eztech.stylyts.domain.models.UserModel
import kz.eztech.stylyts.presentation.contracts.profile.EditProfileContract
import kz.eztech.stylyts.presentation.interfaces.UniversalViewClickListener
import kz.eztech.stylyts.presentation.presenters.profile.EditProfilePresenter
import kz.eztech.stylyts.presentation.utils.EMPTY_STRING
import kz.eztech.stylyts.presentation.utils.FileUtils
import kz.eztech.stylyts.presentation.utils.extensions.getShortName
import kz.eztech.stylyts.presentation.utils.extensions.hide
import kz.eztech.stylyts.presentation.utils.extensions.show
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import javax.inject.Inject

/**
 * Created by Ruslan Erdenoff on 03.03.2021.
 */
class EditProfileDialog(
    private val editorListener: EditorListener
) : DialogFragment(), EditProfileContract.View, View.OnClickListener, UniversalViewClickListener {

    @Inject
    lateinit var presenter: EditProfilePresenter

    private var currentFirstName: String = EMPTY_STRING
    private var currentLastName: String = EMPTY_STRING
    private var currentUserName: String = EMPTY_STRING
    private var currentAvatar: String = EMPTY_STRING
    private var avatarMultipartBody: MultipartBody.Part? = null

    private lateinit var imageResultLaunch: ActivityResultLauncher<Intent>

    companion object {
        private const val TOKEN_ARGS_KEY = "token_args_key"
        private const val NAME_ARGS_KEY = "name_args_key"
        private const val SURNAME_ARGS_KEY = "surname_args_key"
        private const val USERNAME_ARGS_KEY = "username_args_key"

        fun getNewInstance(
            token: String?,
            name: String,
            surname: String,
            username: String,
            editorListener: EditorListener
        ): EditProfileDialog {
            val editProfileDialog = EditProfileDialog(editorListener)
            val args = Bundle()

            args.putString(TOKEN_ARGS_KEY, token)
            args.putString(NAME_ARGS_KEY, name)
            args.putString(SURNAME_ARGS_KEY, surname)
            args.putString(USERNAME_ARGS_KEY, username)
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
        initializeArguments()
        initializeViewsData()
        initializeViews()
        initializeListeners()
        initializePresenter()
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
            toolbar_back_text_view.setOnClickListener {
                dismiss()
            }

            toolbar_title_text_view.show()
            toolbar_title_text_view.text = context.getString(R.string.toolbar_title_edit_profile)

            toolbar_right_corner_action_image_button.hide()

            toolbar_right_text_text_view.show()
            toolbar_right_text_text_view.text = context.getString(R.string.toolbar_completed)
            toolbar_right_text_text_view.setTextColor(
                ContextCompat.getColor(
                    context,
                    R.color.app_light_orange
                )
            )
            toolbar_right_text_text_view.setOnClickListener {
                completeEditing()
            }
        }
    }

    override fun initializeDependency() {
        (requireContext().applicationContext as StylytsApp).applicationComponent.inject(this)
    }

    override fun initializePresenter() = presenter.attach(view = this)

    override fun initializeArguments() {
        arguments?.let {
            if (it.containsKey(NAME_ARGS_KEY)) {
                currentFirstName = it.getString(NAME_ARGS_KEY, EMPTY_STRING)
            }

            if (it.containsKey(USERNAME_ARGS_KEY)) {
                currentUserName = it.getString(USERNAME_ARGS_KEY, EMPTY_STRING)
            }

            if (it.containsKey(SURNAME_ARGS_KEY)) {
                currentLastName = it.getString(SURNAME_ARGS_KEY, EMPTY_STRING)
            }
        }
    }

    override fun initializeViewsData() {
        imageResultLaunch = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                text_view_fragment_profile_edit_user_short_name.hide()
                shapeable_image_view_fragment_profile_avatar.setImageURI(result.data?.data)

                val bitmap = (shapeable_image_view_fragment_profile_avatar.drawable as BitmapDrawable).bitmap
                val file  = FileUtils.createPngFileFromBitmap(requireContext(), bitmap)
                val requestFile = file?.asRequestBody(("image/*").toMediaTypeOrNull())

                requestFile?.let {
                    avatarMultipartBody = MultipartBody.Part.createFormData("avatar", file.name, it)
                }
            }
        }
    }

    override fun initializeViews() {
        if (currentFirstName.isNotEmpty() && currentLastName.isNotEmpty()) {
            edit_text_dialog_edit_profile_name.setText("$currentFirstName $currentLastName")
        }

        if (currentUserName.isNotEmpty()) {
            edit_text_dialog_edit_profile_username.setText(currentUserName)
        }

        if (currentFirstName.isNotEmpty() && currentLastName.isNotEmpty()) {
            text_view_fragment_profile_edit_user_short_name.text =
                getShortName(currentFirstName, currentLastName)
        }
    }

    override fun initializeListeners() {
        dialog_edit_profile_change_photo_text_view.setOnClickListener(this)
    }

    override fun processPostInitialization() {}

    override fun disposeRequests() {}

    override fun displayMessage(msg: String) {
        view?.let {
            Snackbar.make(it, msg, Snackbar.LENGTH_SHORT).apply {
                setBackgroundTint(ContextCompat.getColor(context, R.color.app_dark_blue_gray))
                view.setPadding(0, 0, 0, 0)
            }.show()
        }
    }

    override fun isFragmentVisible(): Boolean = isVisible

    override fun displayProgress() {}

    override fun hideProgress() {}

    override fun successEditing(userModel: UserModel) {
        currentFirstName = userModel.firstName ?: EMPTY_STRING
        editorListener.completeEditing(isSuccess = true)

        dismiss()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.dialog_edit_profile_change_photo_text_view -> {
                ChangeProfilePhotoDialog(universalViewClickListener = this).show(
                    childFragmentManager, EMPTY_STRING
                )
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
        }
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK && requestCode == 0) {
            shapeable_image_view_fragment_profile_avatar.setImageURI(data?.data)
            Log.d("TAG", "${data?.data}")
        }
    }

    private fun completeEditing() {
        val data = HashMap<String, Any?>()
        val names: List<String> = edit_text_dialog_edit_profile_name.text.split(" ")
        val webSite: String = edit_text_dialog_edit_profile_site.text.toString()
        val instagram: String = edit_text_dialog_edit_profile_insta.text.toString()

        if (names.size == 2) {
            if (names[0].isNotBlank()) {
                data["first_name"] = names[0]
            } else {
                displayMessage(msg = "Заполните имя и фамилию через пробел!")

                return
            }
            if (names[1].isNotBlank()) {
                data["last_name"] = names[1]
            } else {
                displayMessage(msg = "Заполните имя и фамилию через пробел!")

                return
            }
        } else {
            displayMessage(msg = "Заполните имя и фамилию через пробел!")

            return
        }

        data["web_site"] = when (webSite.isBlank()) {
            true -> null
            false -> webSite
        }
        data["instagram"] = when (instagram.isBlank()) {
            true -> null
            false -> instagram
        }

        data["avatar"] = avatarMultipartBody

        presenter.editProfile(
            token = arguments?.getString(TOKEN_ARGS_KEY) ?: EMPTY_STRING,
            data = data
        )
    }

    private fun openGalleryForImage() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
//        startActivityForResult(intent, 0)

        imageResultLaunch.launch(intent)
    }

    private fun openCameraForImage() {

    }
}