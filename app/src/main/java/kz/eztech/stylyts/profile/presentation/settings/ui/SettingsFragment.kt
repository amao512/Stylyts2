package kz.eztech.stylyts.profile.presentation.settings.ui

import android.content.Intent
import android.view.View
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.base_toolbar.*
import kotlinx.android.synthetic.main.fragment_settings.*
import kz.eztech.stylyts.R
import kz.eztech.stylyts.global.data.constants.SharedConstants
import kz.eztech.stylyts.auth.presentation.ui.AuthorizationActivity
import kz.eztech.stylyts.global.presentation.common.ui.MainActivity
import kz.eztech.stylyts.global.presentation.base.BaseFragment
import kz.eztech.stylyts.global.presentation.base.BaseView
import kz.eztech.stylyts.global.presentation.common.contracts.EmptyContract
import kz.eztech.stylyts.profile.presentation.settings.ui.dialogs.ExitDialog
import kz.eztech.stylyts.global.presentation.common.interfaces.UniversalViewClickListener
import kz.eztech.stylyts.utils.EMPTY_STRING
import kz.eztech.stylyts.utils.extensions.show

class SettingsFragment : BaseFragment<MainActivity>(), EmptyContract.View, View.OnClickListener,
    UniversalViewClickListener {

    override fun getLayoutId(): Int = R.layout.fragment_settings

    override fun getContractView(): BaseView = this

    override fun customizeActionBar() {
        with(fragment_settings_toolbar) {
            toolbar_left_corner_action_image_button.setBackgroundResource(R.drawable.ic_baseline_keyboard_arrow_left_24)
            toolbar_left_corner_action_image_button.show()

            toolbar_title_text_view.text = context.getString(R.string.settings_title)
            toolbar_title_text_view.show()

            background = ContextCompat.getDrawable(context, R.color.toolbar_bg_gray)
        }
    }

    override fun initializeDependency() {}

    override fun initializePresenter() {}

    override fun initializeArguments() {}

    override fun initializeViewsData() {}

    override fun initializeViews() {
        currentActivity.hideBottomNavigationView()
    }

    override fun initializeListeners() {
        toolbar_left_corner_action_image_button.setOnClickListener(this)
        fragment_settings_saved_items.setOnClickListener(this)
        fragment_settings_personal_settings_item.setOnClickListener(this)
        fragment_settings_exit_item.setOnClickListener(this)
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
            R.id.fragment_settings_saved_items -> {
                findNavController().navigate(R.id.action_settingsFragment_to_savedSettingsFragment)
            }
            R.id.fragment_settings_personal_settings_item -> {
                findNavController().navigate(R.id.action_settingsFragment_to_personalSettingsFragment)
            }
            R.id.fragment_settings_exit_item -> {
                ExitDialog.getNewInstance(
                    universalViewClickListener = this,
                    username = currentActivity.getSharedPrefByKey(SharedConstants.USERNAME_KEY)
                        ?: EMPTY_STRING
                ).show(childFragmentManager, EMPTY_STRING)
            }
        }
    }

    override fun onViewClicked(
        view: View,
        position: Int,
        item: Any?
    ) {
        when (view.id) {
            R.id.dialog_exit_positive_text_view -> onExit()
        }
    }

    private fun onExit() {
        currentActivity.removeFromSharedPrefByKey(SharedConstants.ACCESS_TOKEN_KEY)
        currentActivity.removeFromSharedPrefByKey(SharedConstants.USER_ID_KEY)
        currentActivity.removeFromSharedPrefByKey(SharedConstants.USERNAME_KEY)

        startActivity(Intent(context, AuthorizationActivity::class.java))
        currentActivity.finish()
    }
}