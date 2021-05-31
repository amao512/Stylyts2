package kz.eztech.stylyts.presentation.fragments.ordering

import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.base_toolbar.view.*
import kotlinx.android.synthetic.main.fragment_courtier_ordering.*
import kz.eztech.stylyts.R
import kz.eztech.stylyts.data.api.models.order.DeliveryApiModel
import kz.eztech.stylyts.data.api.models.order.OrderCreateApiModel
import kz.eztech.stylyts.domain.models.order.DeliveryConditionModel
import kz.eztech.stylyts.presentation.activity.MainActivity
import kz.eztech.stylyts.presentation.adapters.ordering.DeliveryConditionAdapter
import kz.eztech.stylyts.presentation.base.BaseFragment
import kz.eztech.stylyts.presentation.base.BaseView
import kz.eztech.stylyts.presentation.contracts.EmptyContract
import kz.eztech.stylyts.presentation.interfaces.UniversalViewClickListener
import kz.eztech.stylyts.presentation.utils.EMPTY_STRING
import kz.eztech.stylyts.presentation.utils.extensions.hide
import kz.eztech.stylyts.presentation.utils.extensions.show

class CourierOrderingFragment : BaseFragment<MainActivity>(), EmptyContract.View,
    View.OnClickListener,
    UniversalViewClickListener {

    private lateinit var deliveryConditionAdapter: DeliveryConditionAdapter

    private lateinit var cityEditText: EditText
    private lateinit var streetEditText: EditText
    private lateinit var houseEditText: EditText
    private lateinit var apartmentEditText: EditText
    private lateinit var otherInfoEditText: EditText
    private lateinit var deliveryConditionsRecyclerView: RecyclerView

    companion object {
        const val CITY_KEY = "city"
        const val CUSTOMER_KEY = "customer"
    }

    override fun onResume() {
        super.onResume()
        currentActivity.hideBottomNavigationView()
    }

    override fun getLayoutId(): Int = R.layout.fragment_courtier_ordering

    override fun getContractView(): BaseView = this

    override fun customizeActionBar() {
        with(fragment_courier_ordering_toolbar) {
            toolbar_left_corner_action_image_button.setImageResource(R.drawable.ic_baseline_keyboard_arrow_left_24)
            toolbar_left_corner_action_image_button.setOnClickListener(this@CourierOrderingFragment)
            toolbar_left_corner_action_image_button.show()

            toolbar_title_text_view.text = getString(R.string.button_ordering)
            toolbar_title_text_view.show()

            toolbar_bottom_border_view.hide()
        }
    }

    override fun initializeDependency() {}

    override fun initializePresenter() {}

    override fun initializeArguments() {}

    override fun initializeViewsData() {
        deliveryConditionAdapter = DeliveryConditionAdapter()
        deliveryConditionAdapter.setOnClickListener(listener = this)
    }

    override fun initializeViews() {
        cityEditText = fragment_select_delivery_way_city_edit_text
        cityEditText.setText(arguments?.getString(CITY_KEY) ?: EMPTY_STRING)

        streetEditText = fragment_ordering_data_street_edit_text
        houseEditText = fragment_ordering_data_house_edit_text
        apartmentEditText = fragment_ordering_data_apartment_edit_text
        otherInfoEditText = fragment_ordering_data_other_edit_text
        deliveryConditionsRecyclerView = fragment_courier_ordering_conditions_recycler_view
        deliveryConditionsRecyclerView.adapter = deliveryConditionAdapter
    }

    override fun initializeListeners() {}

    override fun processPostInitialization() {
        deliveryConditionAdapter.updateList(list = getDeliveryConditions())
    }

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
        }
    }

    override fun onViewClicked(
        view: View,
        position: Int,
        item: Any?
    ) {
        when (item) {
            is DeliveryConditionModel -> onConditionClicked(deliveryConditionModel = item)
        }
    }

    private fun getDeliveryConditions(): List<DeliveryConditionModel> {
        val conditions: MutableList<DeliveryConditionModel> = mutableListOf()

        conditions.add(
            DeliveryConditionModel(
                id = 1,
                title = getString(R.string.with_fitting),
                fittingTime = "10 мин",
                deliveryDate = "25 мая, понедельник"
            )
        )

        conditions.add(
            DeliveryConditionModel(
                id = 2,
                title = getString(R.string.without_fitting),
                fittingTime = "10 мин",
                deliveryDate = "25 мая, понедельник"
            )
        )

        return conditions
    }

    private fun onConditionClicked(deliveryConditionModel: DeliveryConditionModel) {
        if (checkEditTextToValidation()) {
            val bundle = Bundle()

            val delivery = DeliveryApiModel(
                city = cityEditText.text.toString(),
                street = streetEditText.text.toString(),
                house = houseEditText.text.toString(),
                apartment = apartmentEditText.text.toString(),
                deliveryType = when (deliveryConditionModel.id) {
                    1 -> "courier"
                    else -> "pickup"
                }
            )
            val orderCreateApiModel = OrderCreateApiModel(
                itemObjects = emptyList(),
                paymentType = EMPTY_STRING,
                delivery = delivery,
                customer = arguments?.getParcelable(CUSTOMER_KEY)
            )

            bundle.putParcelable(OrderingFragment.ORDER_KEY, orderCreateApiModel)

            findNavController().navigate(
                R.id.action_courierOrderingFragment_to_orderingFragment,
                bundle
            )
        }
    }

    private fun checkEditTextToValidation(): Boolean {
        var flag = true

        if (cityEditText.text.isBlank() || streetEditText.text.isBlank() ||
            houseEditText.text.isBlank() || apartmentEditText.text.isBlank()
        ) {
            flag = false
        }

        if (!flag) {
            displayToast(msg = getString(R.string.empty_fields_message))
        }

        return flag
    }
}