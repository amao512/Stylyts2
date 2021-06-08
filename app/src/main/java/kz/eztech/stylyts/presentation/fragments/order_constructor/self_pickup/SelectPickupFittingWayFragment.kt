package kz.eztech.stylyts.presentation.fragments.order_constructor.self_pickup

import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.base_toolbar.view.*
import kotlinx.android.synthetic.main.fragment_pickup_fitting_way_ordering.*
import kz.eztech.stylyts.R
import kz.eztech.stylyts.domain.models.order.DeliveryWayModel
import kz.eztech.stylyts.presentation.activity.MainActivity
import kz.eztech.stylyts.presentation.adapters.ordering.DeliveryWayAdapter
import kz.eztech.stylyts.presentation.base.BaseFragment
import kz.eztech.stylyts.presentation.base.BaseView
import kz.eztech.stylyts.presentation.contracts.EmptyContract
import kz.eztech.stylyts.presentation.enums.ordering.DeliveryConditionEnum
import kz.eztech.stylyts.presentation.interfaces.UniversalViewClickListener
import kz.eztech.stylyts.presentation.utils.EMPTY_STRING
import kz.eztech.stylyts.presentation.utils.extensions.hide
import kz.eztech.stylyts.presentation.utils.extensions.show

class SelectPickupFittingWayFragment : BaseFragment<MainActivity>(), EmptyContract.View,
    View.OnClickListener,
    UniversalViewClickListener {

    private lateinit var deliveryConditionAdapter: DeliveryWayAdapter

    private lateinit var cityEditText: EditText
    private lateinit var recyclerView: RecyclerView

    companion object {
        const val CITY_KEY = "city"
        const val ORDER_KEY = "order"
    }

    override fun onResume() {
        super.onResume()
        currentActivity.hideBottomNavigationView()
    }

    override fun getLayoutId(): Int = R.layout.fragment_pickup_fitting_way_ordering

    override fun getContractView(): BaseView = this

    override fun customizeActionBar() {
        with(dialog_pickup_point_info_toolbar) {
            toolbar_left_corner_action_image_button.setBackgroundResource(R.drawable.ic_baseline_keyboard_arrow_left_24)
            toolbar_left_corner_action_image_button.setOnClickListener(this@SelectPickupFittingWayFragment)
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
        deliveryConditionAdapter = DeliveryWayAdapter()
        deliveryConditionAdapter.setOnClickListener(listener = this)
    }

    override fun initializeViews() {
        cityEditText = fragment_select_pickup_fitting_way_city_edit_text
        cityEditText.setText(arguments?.getString(CITY_KEY) ?: EMPTY_STRING)

        recyclerView = fragment_select_pickup_fitting_way_recycler_view
        recyclerView.adapter = deliveryConditionAdapter
    }

    override fun initializeListeners() {}

    override fun processPostInitialization() {
        deliveryConditionAdapter.updateList(list = getFittingWayList())
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
            is DeliveryWayModel -> onDeliveryWayClicked(item)
        }
    }

    private fun onDeliveryWayClicked(deliveryWayModel: DeliveryWayModel) {
        val bundle = Bundle()
        bundle.putParcelableArrayList(
            PickupPointsFragment.ORDER_KEY,
            arguments?.getParcelableArrayList(ORDER_KEY)
        )
        bundle.putString(
            PickupPointsFragment.DELIVERY_CONDITION_KEY,
            when (deliveryWayModel.id) {
                1 -> DeliveryConditionEnum.WITH_FITTING.condition
                else -> DeliveryConditionEnum.WITHOUT_FITTING.condition
            }
        )

        findNavController().navigate(
            R.id.action_pickupOrderingFragment_to_pickupPointsFragment,
            bundle
        )
    }

    private fun getFittingWayList(): List<DeliveryWayModel> {
        val deliveryList: MutableList<DeliveryWayModel> = mutableListOf()

        deliveryList.add(
            DeliveryWayModel(
                id = 1,
                icon = R.drawable.ic_clothes_hanger,
                title = getString(R.string.dressing_way_need)
            )
        )

        deliveryList.add(
            DeliveryWayModel(
                id = 2,
                icon = R.drawable.ic_shopping_bag,
                title = getString(R.string.dressing_way_not_need)
            )
        )

        return deliveryList
    }
}