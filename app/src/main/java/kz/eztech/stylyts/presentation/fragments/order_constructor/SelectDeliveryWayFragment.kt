package kz.eztech.stylyts.presentation.fragments.order_constructor

import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.base_toolbar.view.*
import kotlinx.android.synthetic.main.fragment_select_delivery_way.*
import kz.eztech.stylyts.R
import kz.eztech.stylyts.StylytsApp
import kz.eztech.stylyts.data.api.models.order.CustomerApiModel
import kz.eztech.stylyts.data.api.models.order.OrderCreateApiModel
import kz.eztech.stylyts.data.db.cart.CartEntity
import kz.eztech.stylyts.domain.models.order.DeliveryWayModel
import kz.eztech.stylyts.presentation.activity.MainActivity
import kz.eztech.stylyts.presentation.adapters.ordering.DeliveryWayAdapter
import kz.eztech.stylyts.presentation.base.BaseFragment
import kz.eztech.stylyts.presentation.base.BaseView
import kz.eztech.stylyts.presentation.contracts.ordering.SelectDeliveryWayContract
import kz.eztech.stylyts.presentation.enums.ordering.PaymentTypeEnum
import kz.eztech.stylyts.presentation.fragments.order_constructor.courier.CourierOrderingFragment
import kz.eztech.stylyts.presentation.fragments.order_constructor.post.PostOrderingFragment
import kz.eztech.stylyts.presentation.fragments.order_constructor.self_pickup.SelectPickupFittingWayFragment
import kz.eztech.stylyts.presentation.interfaces.UniversalViewClickListener
import kz.eztech.stylyts.presentation.presenters.ordering.SelectDeliveryWayPresenter
import kz.eztech.stylyts.presentation.utils.EMPTY_STRING
import kz.eztech.stylyts.presentation.utils.extensions.hide
import kz.eztech.stylyts.presentation.utils.extensions.show
import java.util.*
import javax.inject.Inject

class SelectDeliveryWayFragment : BaseFragment<MainActivity>(), SelectDeliveryWayContract.View, View.OnClickListener,
    UniversalViewClickListener {

    @Inject lateinit var presenter: SelectDeliveryWayPresenter
    private lateinit var deliveryWayAdapter: DeliveryWayAdapter

    private lateinit var cityEditText: EditText
    private lateinit var recyclerView: RecyclerView

    private var orderList = ArrayList<OrderCreateApiModel>()

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

    override fun initializeDependency() {
        (currentActivity.application as StylytsApp).applicationComponent.inject(fragment = this)
    }

    override fun initializePresenter() {
        presenter.attach(view = this)
    }

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
        presenter.getGoodsFromCart()
    }

    override fun disposeRequests() {
        presenter.disposeRequests()
    }

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

    override fun processGoods(list: List<CartEntity>) {
        val customer = arguments?.getParcelable<CustomerApiModel>(CUSTOMER_KEY)

        list.map { cart ->
            val order = orderList.find { it.ownerId == cart.ownerId }
            val ids: MutableList<Int> = mutableListOf()

            if (order != null) {
                ids.addAll(order.itemObjects)
                ids.add(cart.id!!)

                order.itemObjects = ids
            } else {
                val newOrder = OrderCreateApiModel(
                    itemObjects = arrayListOf(cart.id!!),
                    paymentType = EMPTY_STRING,
                    customer = customer
                )

                newOrder.ownerId = cart.ownerId ?: 0

                orderList.add(newOrder)
            }
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

        when (deliveryWayModel.id) {
            1 -> {
                bundle.putString(CourierOrderingFragment.CITY_KEY, cityEditText.text.toString())
                bundle.putParcelableArrayList(CourierOrderingFragment.ORDER_KEY, orderList)

                findNavController().navigate(
                    R.id.action_selectDeliveryWayFragment_to_courierOrderingFragment,
                    bundle
                )
            }
            2 -> {
                bundle.putString(SelectPickupFittingWayFragment.CITY_KEY, cityEditText.text.toString())
                bundle.putParcelableArrayList(SelectPickupFittingWayFragment.ORDER_KEY, orderList)

                findNavController().navigate(
                    R.id.action_selectDeliveryWayFragment_to_selectPickupFittingWayFragment,
                    bundle
                )
            }
            3 -> {
                bundle.putString(PostOrderingFragment.CITY_KEY, cityEditText.text.toString())
                bundle.putParcelableArrayList(PostOrderingFragment.ORDER_KEY, orderList)

                findNavController().navigate(
                    R.id.action_selectDeliveryWayFragment_to_postOrderingFragment,
                    bundle
                )
            }
        }
    }
}