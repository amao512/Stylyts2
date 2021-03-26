package kz.eztech.stylyts.presentation.fragments.settings

import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.base_toolbar.*
import kotlinx.android.synthetic.main.fragment_personal_settings.*
import kz.eztech.stylyts.R
import kz.eztech.stylyts.presentation.activity.MainActivity
import kz.eztech.stylyts.presentation.base.BaseFragment
import kz.eztech.stylyts.presentation.base.BaseView
import kz.eztech.stylyts.presentation.contracts.EmptyContract
import kz.eztech.stylyts.presentation.dialogs.settings.*
import kz.eztech.stylyts.presentation.interfaces.MessageProblemViewListener
import kz.eztech.stylyts.presentation.utils.EMPTY_STRING
import kz.eztech.stylyts.presentation.utils.extensions.show
import kz.eztech.stylyts.presentation.interfaces.UniversalViewClickListener

class PersonalSettingsFragment : BaseFragment<MainActivity>(), EmptyContract.View,
    View.OnClickListener,
    UniversalViewClickListener, MessageProblemViewListener {

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
        fragment_personal_settings_tell_problem.setOnClickListener(this)
        fragment_personal_settings_notifications.setOnClickListener(this)
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
                childFragmentManager, EMPTY_STRING
            )
            R.id.fragment_personal_settings_password -> ChangePasswordDialog().show(
                childFragmentManager, EMPTY_STRING
            )
            R.id.fragment_personal_settings_tell_problem -> ReportProblemDialog.getNewInstance(
                universalViewClickListener = this
            ).show(
                childFragmentManager, EMPTY_STRING
            )
            R.id.fragment_personal_settings_notifications -> PushNotificationDialog().show(
                childFragmentManager, EMPTY_STRING
            )
        }
    }

    override fun onViewClicked(
        view: View,
        position: Int,
        item: Any?
    ) {
        when (view.id) {
            R.id.dialog_report_problem_spam_text_view -> MessageProblemDialog.getNewInstance(
                problemTitle = getString(R.string.send_problem_spam),
                messageProblemViewListener = this
            ).show(childFragmentManager, EMPTY_STRING)
            R.id.dialog_report_problem_something_wrong_text_view -> MessageProblemDialog.getNewInstance(
                problemTitle = getString(R.string.send_problem_something_went_wrong),
                messageProblemViewListener = this
            ).show(childFragmentManager, EMPTY_STRING)
        }
    }

    override fun writeProblem(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}