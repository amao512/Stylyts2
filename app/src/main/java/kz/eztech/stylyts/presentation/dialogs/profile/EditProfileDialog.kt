package kz.eztech.stylyts.presentation.dialogs.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import kotlinx.android.synthetic.main.base_toolbar.view.*
import kotlinx.android.synthetic.main.dialog_edit_profile.*
import kz.eztech.stylyts.R
import kz.eztech.stylyts.StylytsApp
import kz.eztech.stylyts.presentation.base.EditorListener
import kz.eztech.stylyts.domain.models.UserModel
import kz.eztech.stylyts.presentation.contracts.profile.EditProfileContract
import kz.eztech.stylyts.presentation.presenters.profile.EditProfilePresenter
import kz.eztech.stylyts.presentation.utils.EMPTY_STRING
import kz.eztech.stylyts.presentation.utils.extensions.getShortName
import kz.eztech.stylyts.presentation.utils.extensions.hide
import kz.eztech.stylyts.presentation.utils.extensions.show
import javax.inject.Inject

/**
 * Created by Ruslan Erdenoff on 03.03.2021.
 */
class EditProfileDialog(
    private val editorListener: EditorListener
) : DialogFragment(), EditProfileContract.View, View.OnClickListener {

    @Inject lateinit var presenter: EditProfilePresenter

    private var currentName: String = EMPTY_STRING
    private var currentSurname: String = EMPTY_STRING
    private var currentUserName: String = EMPTY_STRING

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
            toolbar_right_text_text_view.setTextColor(ContextCompat.getColor(context, R.color.app_light_orange))
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
                currentName = it.getString(NAME_ARGS_KEY, EMPTY_STRING)
            }

            if (it.containsKey(USERNAME_ARGS_KEY)) {
                currentUserName = it.getString(USERNAME_ARGS_KEY, EMPTY_STRING)
            }

            if (it.containsKey(SURNAME_ARGS_KEY)) {
                currentSurname = it.getString(SURNAME_ARGS_KEY, EMPTY_STRING)
            }
        }
    }

    override fun initializeViewsData() {}

    override fun initializeViews() {
        if (currentName.isNotEmpty() && currentSurname.isNotEmpty()) {
            edit_text_dialog_edit_profile_name.setText(currentName)
        }

        if (currentUserName.isNotEmpty()) {
            edit_text_dialog_edit_profile_username.setText(currentUserName)
        }

        if (currentName.isNotEmpty() && currentSurname.isNotEmpty()) {
            text_view_fragment_profile_edit_user_short_name.text = getShortName(currentName, currentSurname)
        }
    }

    override fun initializeListeners() {
        dialog_edit_profile_change_photo_text_view.setOnClickListener(this)
    }

    override fun processPostInitialization() {}

    override fun disposeRequests() {}

    override fun displayMessage(msg: String) {}

    override fun isFragmentVisible(): Boolean = isVisible

    override fun displayProgress() {}

    override fun hideProgress() {}

    override fun successEditing(userModel: UserModel) {
        currentName = userModel.name ?: EMPTY_STRING
        editorListener.completeEditing(isSuccess = true)

        dismiss()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.dialog_edit_profile_change_photo_text_view -> ChangeProfilePhotoDialog().show(
                childFragmentManager, EMPTY_STRING
            )
        }
    }

    private fun completeEditing() {
        val data = HashMap<String, Any>()
        val name: String = edit_text_dialog_edit_profile_name.text.toString()

        if (name.isNotBlank()) {
            data["name"] = name

            presenter.editProfile(
                token = arguments?.getString(TOKEN_ARGS_KEY) ?: EMPTY_STRING,
                data = data
            )
        }
    }
}