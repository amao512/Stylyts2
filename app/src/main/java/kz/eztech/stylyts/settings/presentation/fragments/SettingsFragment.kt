package kz.eztech.stylyts.settings.presentation.fragments

import android.view.View
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.base_toolbar.*
import kotlinx.android.synthetic.main.fragment_settings.*
import kz.eztech.stylyts.R
import kz.eztech.stylyts.common.presentation.activity.MainActivity
import kz.eztech.stylyts.common.presentation.base.BaseFragment
import kz.eztech.stylyts.common.presentation.base.BaseView
import kz.eztech.stylyts.common.presentation.contracts.EmptyContract
import kz.eztech.stylyts.common.presentation.utils.extensions.show

class SettingsFragment : BaseFragment<MainActivity>(), EmptyContract.View, View.OnClickListener {

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
        fragment_settings_saved_items.setItem(
            title = getString(R.string.settings_saved),
            icon = R.drawable.ic_saved
        )
        fragment_settings_personal_settings_item.setItem(
            title = getString(R.string.settings_personal_settings),
            icon = R.drawable.ic_settings
        )
        fragment_settings_confidentiality_item.setItem(
            title = getString(R.string.settings_confidentiality),
            icon = R.drawable.ic_lock
        )
        fragment_settings_rule_using_item.setItem(
            title = getString(R.string.settings_rule_using),
            icon = R.drawable.ic_article
        )
        fragment_settings_estimate_app_item.setItem(
            title = getString(R.string.settings_estimate_app),
            icon = R.drawable.ic_star
        )
        fragment_settings_exit_item.setItem(
            title = getString(R.string.settings_exit),
            icon = R.drawable.ic_exit
        )
        fragment_settings_exit_item.setTitleColor(R.color.app_red)
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
                findNavController().navigate(R.id.action_settingsFragment_to_exitDialog)
            }
        }
    }
}