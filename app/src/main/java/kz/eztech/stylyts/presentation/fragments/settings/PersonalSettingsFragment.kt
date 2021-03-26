package kz.eztech.stylyts.presentation.fragments.settings

import android.view.View
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.base_toolbar.*
import kotlinx.android.synthetic.main.fragment_personal_settings.*
import kz.eztech.stylyts.R
import kz.eztech.stylyts.presentation.activity.MainActivity
import kz.eztech.stylyts.presentation.base.BaseFragment
import kz.eztech.stylyts.presentation.base.BaseView
import kz.eztech.stylyts.presentation.contracts.EmptyContract
import kz.eztech.stylyts.presentation.utils.EMPTY_STRING
import kz.eztech.stylyts.presentation.utils.extensions.show
import kz.eztech.stylyts.presentation.dialogs.settings.ChangeLanguageDialog
import kz.eztech.stylyts.presentation.dialogs.settings.ChangePasswordDialog

class PersonalSettingsFragment : BaseFragment<MainActivity>(), EmptyContract.View, View.OnClickListener {

    override fun getLayoutId(): Int = R.layout.fragment_personal_settings

    override fun getContractView(): BaseView = this

    override fun customizeActionBar() {
        with(fragment_personal_settings_toolbar) {
            toolbar_left_corner_action_image_button.setBackgroundResource(R.drawable.ic_baseline_keyboard_arrow_left_24)
            toolbar_left_corner_action_image_button.show()

            toolbar_title_text_view.text = context.getString(R.string.personal_settings_title)
            toolbar_title_text_view.show()

            background = ContextCompat.getDrawable(context, R.color.toolbar_bg_gray)
        }
    }

    override fun initializeDependency() {}

    override fun initializePresenter() {}

    override fun initializeArguments() {}

    override fun initializeViewsData() {}

    override fun initializeViews() {}

    override fun initializeListeners() {
        toolbar_left_corner_action_image_button.setOnClickListener(this)
        fragment_personal_settings_language.setOnClickListener(this)
        fragment_personal_settings_password.setOnClickListener(this)
    }

    override fun processPostInitialization() {}

    override fun disposeRequests() {}

    override fun displayMessage(msg: String) {}

    override fun isFragmentVisible(): Boolean = isVisible

    override fun displayProgress() {}

    override fun hideProgress() {}

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.toolbar_left_corner_action_image_button -> findNavController().navigateUp()
            R.id.fragment_personal_settings_language -> ChangeLanguageDialog().show(
                childFragmentManager,
                EMPTY_STRING
            )
            R.id.fragment_personal_settings_password -> ChangePasswordDialog().show(
                childFragmentManager,
                EMPTY_STRING
            )
        }
    }
}