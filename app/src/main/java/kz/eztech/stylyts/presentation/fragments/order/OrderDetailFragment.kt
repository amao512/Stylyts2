package kz.eztech.stylyts.presentation.fragments.order

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.base_toolbar.view.*
import kotlinx.android.synthetic.main.fragment_order_detail.*
import kotlinx.android.synthetic.main.item_order_status.view.*
import kz.eztech.stylyts.R
import kz.eztech.stylyts.StylytsApp
import kz.eztech.stylyts.domain.models.order.OrderModel
import kz.eztech.stylyts.presentation.activity.MainActivity
import kz.eztech.stylyts.presentation.adapters.ordering.OrderAdapter
import kz.eztech.stylyts.presentation.base.BaseFragment
import kz.eztech.stylyts.presentation.base.BaseView
import kz.eztech.stylyts.presentation.contracts.ordering.OrderDetailContract
import kz.eztech.stylyts.presentation.enums.ordering.DeliveryStatusEnum
import kz.eztech.stylyts.presentation.enums.ordering.DeliveryTypeEnum
import kz.eztech.stylyts.presentation.enums.ordering.OrderStatusEnum
import kz.eztech.stylyts.presentation.enums.ordering.PaymentStatusEnum
import kz.eztech.stylyts.presentation.fragments.order_constructor.PaymentFragment
import kz.eztech.stylyts.presentation.presenters.ordering.OrderDetailPresenter
import kz.eztech.stylyts.presentation.utils.extensions.getFormattedDate
import kz.eztech.stylyts.presentation.utils.extensions.hide
import kz.eztech.stylyts.presentation.utils.extensions.show
import java.text.NumberFormat
import javax.inject.Inject

class OrderDetailFragment : BaseFragment<MainActivity>(), OrderDetailContract.View {

    @Inject lateinit var presenter: OrderDetailPresenter
    private lateinit var clothesAdapter: OrderAdapter

    private lateinit var recyclerView: RecyclerView
    private lateinit var goodsPriceTextView: TextView
    private lateinit var deliveryPriceTextView: TextView
    private lateinit var totalPriceTextView: TextView
    private lateinit var payButton: Button
    private lateinit var deliveryTypeTitleTextView: TextView
    private lateinit var addressTextView: TextView
    private lateinit var notPaidStatusHolder: ConstraintLayout
    private lateinit var paidStatusHolder: ConstraintLayout
    private lateinit var deliveredStatusHolder: ConstraintLayout

    companion object {
        const val ORDER_ID_KEY = "orderId"
    }

    override fun getLayoutId(): Int = R.layout.fragment_order_detail

    override fun getContractView(): BaseView = this

    override fun customizeActionBar() {
        with (fragment_order_detail_toolbar) {
            toolbar_left_corner_action_image_button.setImageResource(R.drawable.ic_baseline_keyboard_arrow_left_24)
            toolbar_left_corner_action_image_button.show()
            toolbar_title_text_view.show()

            customizeActionToolBar(toolbar = this, title = getString(R.string.order_number_text_format))
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
        clothesAdapter = OrderAdapter()
    }

    override fun initializeViews() {
        recyclerView = fragment_order_detail_recycler_view
        recyclerView.adapter = clothesAdapter

        goodsPriceTextView = fragment_order_detail_goods_price_text_view
        deliveryPriceTextView = fragment_order_detail_delivery_price_text_view
        totalPriceTextView = fragment_order_detail_total_price_text_view
        payButton = fragment_order_detail_pay_button
        deliveryTypeTitleTextView = fragment_order_detail_delivery_type_title_text_view
        addressTextView = fragment_order_detail_address_text_view
        notPaidStatusHolder = fragment_order_not_paid_status_holder_constraint_layout
        paidStatusHolder = fragment_order_detail_paid_status_holder as ConstraintLayout
        deliveredStatusHolder = fragment_order_detail_delivered_status_holder as ConstraintLayout
    }

    override fun initializeListeners() {}

    override fun processPostInitialization() {
        presenter.getOrderById(
            token = currentActivity.getTokenFromSharedPref(),
            orderId = arguments?.getInt(ORDER_ID_KEY) ?: 0
        )
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

    override fun processOrder(orderModel: OrderModel) {
        fragment_order_detail_toolbar.toolbar_title_text_view.text = getString(
            R.string.order_number_text_format,
            orderModel.id.toString()
        )

        goodsPriceTextView.text = getString(
            R.string.price_tenge_text_format,
            NumberFormat.getInstance().format(orderModel.price)
        )
        totalPriceTextView.text = getString(
            R.string.price_tenge_text_format,
            NumberFormat.getInstance().format(orderModel.price)
        )
        clothesAdapter.updateList(list = orderModel.itemObjects)
        deliveryPriceTextView.text = "0 тг"

        payButton.setOnClickListener {
            val bundle = Bundle()
            bundle.putInt(PaymentFragment.ORDER_ID_KEY, orderModel.id)

            findNavController().navigate(R.id.action_orderDetailFragment_to_paymentFragment, bundle)
        }

        deliveryTypeTitleTextView.text = when (orderModel.delivery.deliveryType) {
            DeliveryTypeEnum.COURIER.type -> getString(R.string.delivery)
            DeliveryTypeEnum.SELF_PICKUP.type -> getString(R.string.delivery_pickup)
            else -> getString(R.string.delivery_way_post)
        }

        addressTextView.text = getString(
            R.string.address_detail_text_format,
            orderModel.delivery.city,
            orderModel.delivery.street,
            orderModel.delivery.house,
            orderModel.delivery.apartment
        )

        setOrderStatus(orderModel)
    }

    private fun setOrderStatus(orderModel: OrderModel) {
        if (orderModel.invoice.paymentStatus == PaymentStatusEnum.PAID.status && orderModel.status == OrderStatusEnum.ACTIVE.status) {
            paidStatusHolder.item_order_status_status_not_paid_text_view.text = getString(R.string.status_paid)
            paidStatusHolder.item_order_status_not_paid_status_date_text_view.text = getFormattedDate(orderModel.createdAt)
            paidStatusHolder.show()
        } else if (orderModel.status == OrderStatusEnum.COMPLETED.status && orderModel.delivery.deliveryStatus == DeliveryStatusEnum.DELIVERED.status) {
            paidStatusHolder.item_order_status_to_next_squares_linear_layout.show()
            deliveredStatusHolder.item_order_status_status_not_paid_text_view.text = getString(R.string.status_delivered)
            deliveredStatusHolder.item_order_status_not_paid_status_date_text_view.text = getFormattedDate(orderModel.createdAt)
            deliveredStatusHolder.show()
        } else if (orderModel.invoice.paymentStatus == PaymentStatusEnum.PENDING.status && orderModel.status == OrderStatusEnum.ACTIVE.status) {
            paidStatusHolder.hide()
            deliveredStatusHolder.hide()
            notPaidStatusHolder.show()
        }
    }
}