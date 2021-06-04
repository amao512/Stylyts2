package kz.eztech.stylyts.presentation.fragments.order_constructor

import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.base_toolbar.view.*
import kotlinx.android.synthetic.main.fragment_select_delivery_way.*
import kz.eztech.stylyts.R
import kz.eztech.stylyts.data.api.models.order.CustomerApiModel
import kz.eztech.stylyts.domain.models.order.DeliveryWayModel
import kz.eztech.stylyts.presentation.activity.MainActivity
import kz.eztech.stylyts.presentation.adapters.ordering.DeliveryWayAdapter
import kz.eztech.stylyts.presentation.base.BaseFragment
import kz.eztech.stylyts.presentation.base.BaseView
import kz.eztech.stylyts.presentation.contracts.EmptyContract
import kz.eztech.stylyts.presentation.interfaces.UniversalViewClickListener
import kz.eztech.stylyts.presentation.utils.extensions.hide
import kz.eztech.stylyts.presentation.utils.extensions.show

class SelectDeliveryWayFragment : BaseFragment<MainActivity>(), EmptyContract.View, View.OnClickListener,
    UniversalViewClickListener {

    private lateinit var deliveryWayAdapter: DeliveryWayAdapter

    private lateinit var cityEditText: EditText
    private lateinit var recyclerView: RecyclerView

    companion object {
        const val CUSTOMER_KEY = "customer"
    }

    override fun onResume() {
        super.onResume()
        currentActivity.hideBottomNavigationView()
    }

    override fun getLayoutId(): Int = R.layout.fragment_select_delivery_way

    override fun getContractView(): BaseView = this

    override fun customizeActionBar() {
        with(fragment_ordering_toolbar) {
            toolbar_left_corner_action_image_button.setBackgroundResource(R.drawable.ic_baseline_keyboard_arrow_left_24)
            toolbar_left_corner_action_image_button.setOnClickListener(this@SelectDeliveryWayFragment)
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
        deliveryWayAdapter = DeliveryWayAdapter()
        deliveryWayAdapter.setOnClickListener(listener = this)
    }

    override fun initializeViews() {
        cityEditText = fragment_select_delivery_way_city_edit_text
        recyclerView = fragment_select_delivery_way_recycler_view
        recyclerView.adapter = deliveryWayAdapter
    }

    override fun initializeListeners() {}

    override fun processPostInitialization() {
        deliveryWayAdapter.updateList(list = getDeliveryWayList())
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

    private fun getDeliveryWayList(): List<DeliveryWayModel> {
        val deliveryList: MutableList<DeliveryWayModel> = mutableListOf()

        deliveryList.add(
            DeliveryWayModel(
                id = 1,
                icon = R.drawable.ic_delivery_car,
                title = getString(R.string.delivery_way_courier)
            )
        )

        deliveryList.add(
            DeliveryWayModel(
                id = 2,
                icon = R.drawable.ic_location_pin,
                title = getString(R.string.delivery_way_pickup)
            )
        )

        deliveryList.add(
            DeliveryWayModel(
                id = 3,
                icon = R.drawable.ic_mail,
                title = getString(R.string.delivery_way_post)
            )
        )

        return deliveryList
    }

    private fun onDeliveryWayClicked(deliveryWayModel: DeliveryWayModel) {
        val bundle = Bundle()
        val customer = arguments?.getParcelable<CustomerApiModel>(CUSTOMER_KEY)

        when (deliveryWayModel.id) {
            1 -> {
                bundle.putString(CourierOrderingFragment.CITY_KEY, cityEditText.text.toString())
                bundle.putParcelable(CourierOrderingFragment.CUSTOMER_KEY, customer)

                findNavController().navigate(
                    R.id.action_selectDeliveryWayFragment_to_courierOrderingFragment,
                    bundle
                )
            }
            2 -> {
                bundle.putString(PickupOrderingFragment.CITY_KEY, cityEditText.text.toString())
                bundle.putParcelable(PickupOrderingFragment.CUSTOMER_KEY, customer)

                findNavController().navigate(
                    R.id.action_selectDeliveryWayFragment_to_pickupOrderingFragment,
                    bundle
                )
            }
            3 -> {
                bundle.putString(PostOrderingFragment.CITY_KEY, cityEditText.text.toString())
                bundle.putParcelable(PostOrderingFragment.CUSTOMER_KEY, customer)

                findNavController().navigate(
                    R.id.action_selectDeliveryWayFragment_to_postOrderingFragment,
                    bundle
                )
            }
        }
    }
}