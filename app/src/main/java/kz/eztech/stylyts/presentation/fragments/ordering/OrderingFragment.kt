package kz.eztech.stylyts.presentation.fragments.ordering

import android.view.View
import android.widget.EditText
import androidx.appcompat.widget.SwitchCompat
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.base_toolbar.view.*
import kotlinx.android.synthetic.main.fragment_ordering.*
import kz.eztech.stylyts.R
import kz.eztech.stylyts.presentation.activity.MainActivity
import kz.eztech.stylyts.presentation.base.BaseFragment
import kz.eztech.stylyts.presentation.base.BaseView
import kz.eztech.stylyts.presentation.contracts.EmptyContract
import kz.eztech.stylyts.presentation.utils.extensions.hide
import kz.eztech.stylyts.presentation.utils.extensions.show

class OrderingFragment : BaseFragment<MainActivity>(), EmptyContract.View, View.OnClickListener {

    private lateinit var nameEditText: EditText
    private lateinit var surnameEditText: EditText
    private lateinit var phoneEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var subsMailingSwitchCompat: SwitchCompat

    override fun onResume() {
        super.onResume()
        currentActivity.hideBottomNavigationView()
    }

    override fun getLayoutId(): Int = R.layout.fragment_ordering

    override fun getContractView(): BaseView = this

    override fun customizeActionBar() {
        with (fragment_ordering_toolbar) {
            toolbar_left_corner_action_image_button.setBackgroundResource(R.drawable.ic_baseline_keyboard_arrow_left_24)
            toolbar_left_corner_action_image_button.show()
            toolbar_left_corner_action_image_button.setOnClickListener(this@OrderingFragment)

            toolbar_right_text_text_view.text = getString(R.string.next)
            toolbar_right_text_text_view.setTextColor(
                ContextCompat.getColor(requireContext(), R.color.app_light_orange)
            )
            toolbar_right_text_text_view.show()
            toolbar_right_text_text_view.setOnClickListener(this@OrderingFragment)

            toolbar_title_text_view.text = getString(R.string.button_ordering)
            toolbar_title_text_view.show()

            toolbar_bottom_border_view.hide()
        }
    }

    override fun initializeDependency() {}

    override fun initializePresenter() {}

    override fun initializeArguments() {}

    override fun initializeViewsData() {}

    override fun initializeViews() {
        nameEditText = fragment_ordering_name_edit_text
        surnameEditText = fragment_ordering_surname_edit_text
        phoneEditText = fragment_ordering_mobile_phone_edit_text
        emailEditText = fragment_ordering_email_edit_text
        subsMailingSwitchCompat = fragment_ordering_subs_mailing_switch_compat
    }

    override fun initializeListeners() {}

    override fun processPostInitialization() {}

    override fun disposeRequests() {}

    override fun displayMessage(msg: String) {
        displayToast(msg)
    }

    override fun isFragmentVisible(): Boolean = isVisible

    override fun displayProgress() {}

    override fun hideProgress() {}

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.toolbar_left_corner_action_image_button -> findNavController().navigateUp()
            R.id.toolbar_right_text_text_view -> {
                findNavController().navigate(R.id.action_orderingFragment_to_orderingDataFragment)
            }
        }
    }
}