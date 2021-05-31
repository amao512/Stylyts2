package kz.eztech.stylyts.presentation.fragments.ordering

import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.appcompat.widget.SwitchCompat
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.santalu.maskara.widget.MaskEditText
import kotlinx.android.synthetic.main.base_toolbar.view.*
import kotlinx.android.synthetic.main.fragment_ordering_customer_info.*
import kz.eztech.stylyts.R
import kz.eztech.stylyts.data.api.models.order.CustomerApiModel
import kz.eztech.stylyts.presentation.activity.MainActivity
import kz.eztech.stylyts.presentation.base.BaseFragment
import kz.eztech.stylyts.presentation.base.BaseView
import kz.eztech.stylyts.presentation.contracts.EmptyContract
import kz.eztech.stylyts.presentation.utils.extensions.hide
import kz.eztech.stylyts.presentation.utils.extensions.show

class OrderingCustomerInfoFragment : BaseFragment<MainActivity>(), EmptyContract.View, View.OnClickListener {

    private lateinit var nameEditText: EditText
    private lateinit var surnameEditText: EditText
    private lateinit var phoneEditText: MaskEditText
    private lateinit var emailEditText: EditText
    private lateinit var subsMailingSwitchCompat: SwitchCompat

    override fun onResume() {
        super.onResume()
        currentActivity.hideBottomNavigationView()
    }

    override fun getLayoutId(): Int = R.layout.fragment_ordering_customer_info

    override fun getContractView(): BaseView = this

    override fun customizeActionBar() {
        with (fragment_ordering_customer_info_toolbar) {
            toolbar_left_corner_action_image_button.setBackgroundResource(R.drawable.ic_baseline_keyboard_arrow_left_24)
            toolbar_left_corner_action_image_button.show()
            toolbar_left_corner_action_image_button.setOnClickListener(this@OrderingCustomerInfoFragment)

            toolbar_right_text_text_view.text = getString(R.string.next)
            toolbar_right_text_text_view.setTextColor(
                ContextCompat.getColor(requireContext(), R.color.app_light_orange)
            )
            toolbar_right_text_text_view.show()
            toolbar_right_text_text_view.setOnClickListener(this@OrderingCustomerInfoFragment)

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
        nameEditText = fragment_ordering_customer_info_name_edit_text
        surnameEditText = fragment_ordering_customer_info_surname_edit_text
        phoneEditText = fragment_ordering_customer_info_mobile_phone_edit_text
        emailEditText = fragment_ordering_customer_info_email_edit_text
        subsMailingSwitchCompat = fragment_ordering_customer_info_subs_mailing_switch_compat
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
            R.id.toolbar_right_text_text_view -> onNextClick()
        }
    }

    private fun onNextClick() {
        if (checkInputValidation()) {
            val bundle = Bundle()
            val customer = CustomerApiModel(
                firstName = nameEditText.text.toString(),
                lastName = surnameEditText.text.toString(),
                phoneNumber = phoneEditText.text!!.toString(),
                email = emailEditText.text.toString()
            )

            bundle.putParcelable(SelectDeliveryWayFragment.CUSTOMER_KEY, customer)
            findNavController().navigate(
                R.id.action_customerInfoFragment_to_selectDeliveryWayFragment,
                bundle
            )
        }
    }

    private fun checkInputValidation(): Boolean {
        var flag = true

        if (nameEditText.text.isBlank() || surnameEditText.text.isBlank() ||
                phoneEditText.text!!.isBlank() || emailEditText.text.isBlank()) {
            flag = false
        }

        if (!flag) {
            displayToast(msg = getString(R.string.empty_fields_message))
        }

        return flag
    }
}