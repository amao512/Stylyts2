package kz.eztech.stylyts.presentation.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import kotlinx.android.synthetic.main.base_toolbar.view.*
import kotlinx.android.synthetic.main.dialog_edit_profile.*
import kz.eztech.stylyts.R
import kz.eztech.stylyts.StylytsApp
import kz.eztech.stylyts.presentation.contracts.main.profile.EditProfileContract
import kz.eztech.stylyts.presentation.utils.extensions.EMPTY_STRING
import kz.eztech.stylyts.presentation.utils.extensions.hide
import kz.eztech.stylyts.presentation.utils.extensions.show
import java.util.*

/**
 * Created by Ruslan Erdenoff on 03.03.2021.
 */
class EditProfileDialog : DialogFragment(), EditProfileContract.View {

    private var currentName: String = EMPTY_STRING
    private var currentSurname: String = EMPTY_STRING
    private var currentUserName: String = EMPTY_STRING

    companion object {
        private const val NAME_ARGS_KEY = "name_args_key"
        private const val SURNAME_ARGS_KEY = "surname_args_key"
        private const val USERNAME_ARGS_KEY = "username_args_key"

        fun getNewInstance(
            name: String,
            surname: String
        ): EditProfileDialog {
            val editProfileDialog = EditProfileDialog()
            val args = Bundle()

            args.putString(NAME_ARGS_KEY, name)
            args.putString(SURNAME_ARGS_KEY, surname)
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

    override fun customizeActionBar() {
        with(include_toolbar_edit_profile) {
            image_button_left_corner_action.hide()
            text_view_toolbar_back.show()
            text_view_toolbar_back.text = context.getString(R.string.toolbar_cancel)
            text_view_toolbar_back.setCompoundDrawables(null, null, null, null)
            text_view_toolbar_back.setOnClickListener {
                dismiss()
            }
            text_view_toolbar_title.show()
            text_view_toolbar_title.text = context.getString(R.string.toolbar_title_edit_profile)
            image_button_right_corner_action.hide()
            text_view_toolbar_right_text.show()
            text_view_toolbar_right_text.text = context.getString(R.string.toolbar_completed)
            text_view_toolbar_right_text.setOnClickListener {
                dismiss()
            }
            elevation = 0f
        }
    }

    override fun initializeDependency() {
        (requireContext().applicationContext as StylytsApp).applicationComponent.inject(this)
    }

    override fun initializePresenter() {}

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
        if (currentName.isNotEmpty()) {
            edit_text_dialog_edit_profile_name.setText(currentName)
        }

        if (currentUserName.isNotEmpty()) {
            edit_text_dialog_edit_profile_username.setText(currentUserName)
        }

        if (currentSurname.isNotEmpty()) {
            edit_text_dialog_edit_profile_username.setText(currentSurname)
        }

        if (currentName.isNotEmpty() && currentSurname.isNotEmpty()) {
            text_view_fragment_profile_edit_user_short_name.text = getString(
                R.string.full_name_text_format,
                currentName.toUpperCase(Locale.getDefault())[0],
                currentSurname.toUpperCase(Locale.getDefault())[0]
            )
        }
    }

    override fun initializeListeners() {}

    override fun processPostInitialization() {}

    override fun disposeRequests() {}

    override fun displayMessage(msg: String) {}

    override fun isFragmentVisible(): Boolean = isVisible

    override fun displayProgress() {}

    override fun hideProgress() {}

    override fun getTheme(): Int = R.style.FullScreenDialog
}