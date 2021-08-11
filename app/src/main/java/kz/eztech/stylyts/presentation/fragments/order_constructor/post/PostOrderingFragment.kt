package kz.eztech.stylyts.presentation.fragments.order_constructor.post

import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.base_toolbar.view.*
import kotlinx.android.synthetic.main.fragment_courtier_ordering.*
import kotlinx.android.synthetic.main.fragment_post_ordering.*
import kz.eztech.stylyts.R
import kz.eztech.stylyts.data.api.models.order.DeliveryCreateApiModel
import kz.eztech.stylyts.data.api.models.order.OrderCreateApiModel
import kz.eztech.stylyts.presentation.activity.MainActivity
import kz.eztech.stylyts.presentation.base.BaseFragment
import kz.eztech.stylyts.presentation.base.BaseView
import kz.eztech.stylyts.presentation.contracts.EmptyContract
import kz.eztech.stylyts.presentation.enums.ordering.DeliveryTypeEnum
import kz.eztech.stylyts.presentation.fragments.order_constructor.OrderingFragment
import kz.eztech.stylyts.utils.EMPTY_STRING
import kz.eztech.stylyts.utils.extensions.hide
import kz.eztech.stylyts.utils.extensions.show

class PostOrderingFragment : BaseFragment<MainActivity>(), EmptyContract.View, View.OnClickListener {

    private lateinit var cityEditText: EditText
    private lateinit var streetEditText: EditText
    private lateinit var houseEditText: EditText
    private lateinit var apartmentEditText: EditText
    private lateinit var postIndexEditText: EditText

    companion object {
        const val CITY_KEY = "city"
        const val ORDER_KEY = "order"
    }

    override fun getLayoutId(): Int = R.layout.fragment_post_ordering

    override fun getContractView(): BaseView = this

    override fun customizeActionBar() {
        with (fragment_post_ordering_toolbar) {
            toolbar_left_corner_action_image_button.setImageResource(R.drawable.ic_baseline_keyboard_arrow_left_24)
            toolbar_left_corner_action_image_button.setOnClickListener(this@PostOrderingFragment)
            toolbar_left_corner_action_image_button.show()

            toolbar_title_text_view.text = getString(R.string.button_ordering)
            toolbar_title_text_view.show()

            toolbar_right_text_text_view.text = getString(R.string.next)
            toolbar_right_text_text_view.setTextColor(
                ContextCompat.getColor(requireContext(), R.color.app_light_orange)
            )
            toolbar_right_text_text_view.setOnClickListener(this@PostOrderingFragment)
            toolbar_right_text_text_view.show()

            toolbar_bottom_border_view.hide()
        }
    }

    override fun initializeDependency() {}

    override fun initializePresenter() {}

    override fun initializeArguments() {}

    override fun initializeViewsData() {}

    override fun initializeViews() {
        cityEditText = fragment_post_ordering_city_edit_text
        cityEditText.setText(arguments?.getString(CITY_KEY) ?: EMPTY_STRING)

        streetEditText = fragment_post_ordering_street_edit_text
        houseEditText = fragment_post_ordering_house_edit_text
        apartmentEditText = fragment_post_ordering_apartment_edit_text
        postIndexEditText = fragment_post_ordering_post_index_edit_text

        initializeHideKeyboardView(view = fragment_post_ordering_hide_keyboard)
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
        if (checkEditTextToValidation()) {
            val bundle = Bundle()
            val delivery = DeliveryCreateApiModel(
                city = cityEditText.text.toString(),
                street = streetEditText.text.toString(),
                house = houseEditText.text.toString(),
                apartment = apartmentEditText.text.toString(),
                deliveryType = DeliveryTypeEnum.POST.type
            )
            val orders = arguments?.getParcelableArrayList<OrderCreateApiModel>(ORDER_KEY)

            orders?.map {
                it.delivery = delivery
            }

            bundle.putParcelableArrayList(OrderingFragment.ORDER_KEY, orders)

            findNavController().navigate(R.id.action_postOrderingFragment_to_orderingFragment, bundle)
        }
    }

    private fun checkEditTextToValidation(): Boolean {
        var flag = true

        if (cityEditText.text.isBlank() || streetEditText.text.isBlank() ||
            houseEditText.text.isBlank() || apartmentEditText.text.isBlank()
            || postIndexEditText.text.isBlank()
        ) {
            flag = false
        }

        if (!flag) {
            displayToast(msg = getString(R.string.empty_fields_message))
        }

        return flag
    }
}