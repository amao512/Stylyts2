package kz.eztech.stylyts.presentation.fragments.order

import android.content.res.ColorStateList
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.base_toolbar.view.*
import kotlinx.android.synthetic.main.fragment_order_detail.*
import kotlinx.android.synthetic.main.item_order_status.view.*
import kz.eztech.stylyts.R
import kz.eztech.stylyts.StylytsApp
import kz.eztech.stylyts.domain.models.order.OrderModel
import kz.eztech.stylyts.presentation.activity.MainActivity
import kz.eztech.stylyts.presentation.adapters.ordering.UserOrderAdapter
import kz.eztech.stylyts.presentation.base.BaseFragment
import kz.eztech.stylyts.presentation.base.BaseView
import kz.eztech.stylyts.presentation.contracts.ordering.UserOrderDetailContract
import kz.eztech.stylyts.presentation.enums.ordering.DeliveryStatusEnum
import kz.eztech.stylyts.presentation.enums.ordering.DeliveryTypeEnum
import kz.eztech.stylyts.presentation.enums.ordering.OrderStatusEnum
import kz.eztech.stylyts.presentation.enums.ordering.PaymentStatusEnum
import kz.eztech.stylyts.presentation.fragments.order_constructor.PaymentFragment
import kz.eztech.stylyts.presentation.presenters.ordering.UserOrderDetailPresenter
import kz.eztech.stylyts.presentation.utils.extensions.getFormattedDate
import kz.eztech.stylyts.presentation.utils.extensions.hide
import kz.eztech.stylyts.presentation.utils.extensions.show
import java.text.NumberFormat
import javax.inject.Inject

class UserOrderDetailFragment : BaseFragment<MainActivity>(), UserOrderDetailContract.View {

    @Inject lateinit var presenterUser: UserOrderDetailPresenter
    private lateinit var clothesAdapterUser: UserOrderAdapter

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
    private lateinit var cancelledStatusHolder: ConstraintLayout
    private lateinit var returnedStatusHolder: ConstraintLayout

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
        (currentActivity.application as StylytsApp).applicationComponent.inject(fragmentUser = this)
    }

    override fun initializePresenter() {
        presenterUser.attach(view = this)
    }

    override fun initializeArguments() {}

    override fun initializeViewsData() {
        clothesAdapterUser = UserOrderAdapter()
    }

    override fun initializeViews() {
        recyclerView = fragment_order_detail_recycler_view
        recyclerView.adapter = clothesAdapterUser

        goodsPriceTextView = fragment_order_detail_goods_price_text_view
        deliveryPriceTextView = fragment_order_detail_delivery_price_text_view
        totalPriceTextView = fragment_order_detail_total_price_text_view
        payButton = fragment_order_detail_pay_button
        deliveryTypeTitleTextView = fragment_order_detail_delivery_type_title_text_view
        addressTextView = fragment_order_detail_address_text_view
        notPaidStatusHolder = fragment_order_not_paid_status_holder_constraint_layout
        paidStatusHolder = fragment_order_detail_paid_status_holder as ConstraintLayout
        deliveredStatusHolder = fragment_order_detail_delivered_status_holder as ConstraintLayout
        cancelledStatusHolder = fragment_order_detail_cancelled_status_holder as ConstraintLayout
        returnedStatusHolder = fragment_order_detail_returned_status_holder as ConstraintLayout
    }

    override fun initializeListeners() {}

    override fun processPostInitialization() {
        presenterUser.getOrderById(
            token = currentActivity.getTokenFromSharedPref(),
            orderId = arguments?.getInt(ORDER_ID_KEY) ?: 0
        )
    }

    override fun disposeRequests() {
        presenterUser.disposeRequests()
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
        clothesAdapterUser.updateList(list = orderModel.itemObjects)
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
        val isOrderActive = orderModel.status == OrderStatusEnum.ACTIVE.status
        val isOrderCompleted = orderModel.status == OrderStatusEnum.COMPLETED.status
        val isOrderReturned = orderModel.status == OrderStatusEnum.RETURNED.status
        val isOrderCancelled = orderModel.status == OrderStatusEnum.CANCELLED.status

        val isPaymentPending = orderModel.invoice.paymentStatus == PaymentStatusEnum.PENDING.status
        val isPaymentNew = orderModel.invoice.paymentStatus == PaymentStatusEnum.NEW.status
        val isPaymentPaid = orderModel.invoice.paymentStatus == PaymentStatusEnum.PAID.status
        val isPaymentRefund = orderModel.invoice.paymentStatus == PaymentStatusEnum.REFUND.status

        val isDelivered = orderModel.delivery.deliveryStatus == DeliveryStatusEnum.DELIVERED.status
        val isDeliveryNew = orderModel.delivery.deliveryStatus == DeliveryStatusEnum.NEW.status
        val isDeliveryInProgress = orderModel.delivery.deliveryStatus == DeliveryStatusEnum.IN_PROGRESS.status

        val isOperationUrlNotBlank = orderModel.invoice.operationUrl.isNotBlank()

        if (isOrderActive) {
            if (isPaymentPaid) {
                paidStatusHolder.item_order_status_title_text_view.text = getString(R.string.status_paid)
                paidStatusHolder.item_order_status_date_text_view.text = getFormattedDate(orderModel.createdAt)
                paidStatusHolder.show()
            } else if (
                (isPaymentPending && isOrderActive && isOperationUrlNotBlank) ||
                (isPaymentNew && isOrderActive && isOperationUrlNotBlank)
            ) {
                paidStatusHolder.hide()
                deliveredStatusHolder.hide()
                returnedStatusHolder.hide()
                cancelledStatusHolder.hide()
                notPaidStatusHolder.show()
            }
        } else {
            if (isOrderCompleted && isDelivered) {
                paidStatusHolder.item_order_status_title_text_view.text = getString(R.string.status_paid)
                paidStatusHolder.item_order_status_date_text_view.text = getFormattedDate(orderModel.createdAt)
                paidStatusHolder.item_order_status_to_next_squares_linear_layout.show()
                paidStatusHolder.show()

                deliveredStatusHolder.item_order_status_title_text_view.text = getString(R.string.status_delivered)
                deliveredStatusHolder.item_order_status_date_text_view.text = getFormattedDate(orderModel.createdAt)
                deliveredStatusHolder.show()
            } else if (isOrderCancelled) {
                paidStatusHolder.hide()
                deliveredStatusHolder.hide()
                notPaidStatusHolder.hide()
                returnedStatusHolder.hide()

                cancelledStatusHolder.item_order_status_icon_image_view.backgroundTintList = ColorStateList.valueOf(
                    ContextCompat.getColor(requireContext(), R.color.app_light_orange)
                )
                cancelledStatusHolder.item_order_status_icon_image_view.setImageResource(R.drawable.ic_baseline_close_white_24)
                cancelledStatusHolder.item_order_status_title_text_view.text = getString(R.string.status_cancelled)
                cancelledStatusHolder.item_order_status_date_text_view.text = getFormattedDate(orderModel.createdAt)
                cancelledStatusHolder.show()
            } else if (isOrderReturned) {
                paidStatusHolder.hide()
                deliveredStatusHolder.hide()
                notPaidStatusHolder.hide()
                cancelledStatusHolder.hide()

                returnedStatusHolder.item_order_status_icon_image_view.backgroundTintList = ColorStateList.valueOf(
                    ContextCompat.getColor(requireContext(), R.color.app_light_orange)
                )
                returnedStatusHolder.item_order_status_icon_image_view.setImageResource(R.drawable.ic_baseline_close_white_24)
                returnedStatusHolder.item_order_status_title_text_view.text = getString(R.string.status_returned)
                returnedStatusHolder.item_order_status_date_text_view.text = getFormattedDate(orderModel.createdAt)
                returnedStatusHolder.show()
            }
        }
    }
}