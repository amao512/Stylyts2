package kz.eztech.stylyts.presentation.fragments.order

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.View
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
import kz.eztech.stylyts.domain.models.clothes.ClothesModel
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
import kz.eztech.stylyts.presentation.fragments.clothes.ClothesDetailFragment
import kz.eztech.stylyts.presentation.fragments.order_constructor.PaymentFragment
import kz.eztech.stylyts.presentation.interfaces.UniversalViewClickListener
import kz.eztech.stylyts.presentation.presenters.ordering.UserOrderDetailPresenter
import kz.eztech.stylyts.presentation.utils.extensions.getDayAndMonth
import kz.eztech.stylyts.presentation.utils.extensions.hide
import kz.eztech.stylyts.presentation.utils.extensions.show
import javax.inject.Inject

class UserOrderDetailFragment : BaseFragment<MainActivity>(), UserOrderDetailContract.View, UniversalViewClickListener {

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
    private lateinit var appliedStatusHolder: ConstraintLayout

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
        clothesAdapterUser.setOnClickListener(listener = this)
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
        appliedStatusHolder = fragment_order_detail_applied_status_holder as ConstraintLayout
    }

    override fun initializeListeners() {}

    override fun processPostInitialization() {
        presenterUser.getOrderById(orderId = arguments?.getInt(ORDER_ID_KEY) ?: 0)
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

    override fun onViewClicked(
        view: View,
        position: Int,
        item: Any?
    ) {
        when (item) {
            is ClothesModel -> navigateToClothes(item)
        }
    }

    override fun processOrder(orderModel: OrderModel) = with (orderModel) {
        fragment_order_detail_toolbar.toolbar_title_text_view.text = getString(
            R.string.order_number_text_format,
            id.toString()
        )

        goodsPriceTextView.text = displayPrice
        totalPriceTextView.text = displayPrice
        clothesAdapterUser.updateList(list = itemObjects)
        deliveryPriceTextView.text = "0 ₸"

        payButton.setOnClickListener {
            val bundle = Bundle()
            bundle.putInt(PaymentFragment.ORDER_ID_KEY, id)

            findNavController().navigate(R.id.action_orderDetailFragment_to_paymentFragment, bundle)
        }

        deliveryTypeTitleTextView.text = when (delivery.deliveryType) {
            DeliveryTypeEnum.COURIER.type -> getString(R.string.delivery)
            DeliveryTypeEnum.SELF_PICKUP.type -> getString(R.string.delivery_pickup)
            else -> getString(R.string.delivery_way_post)
        }

        addressTextView.text = getString(
            R.string.address_detail_text_format,
            delivery.city,
            delivery.street,
            delivery.house,
            delivery.apartment
        )

        setOrderStatus(orderModel)
    }

    private fun setOrderStatus(orderModel: OrderModel) {
        if (orderModel.status == OrderStatusEnum.ACTIVE.status) {
            processActiveStatus(orderModel)
        } else {
            processNotActiveStatus(orderModel)
        }
    }

    private fun processActiveStatus(orderModel: OrderModel) = with (orderModel) {
        val isPaymentNew = invoice.paymentStatus == PaymentStatusEnum.NEW.status
        val isPaymentPaid = invoice.paymentStatus == PaymentStatusEnum.PAID.status
        val isPaymentPending = invoice.paymentStatus == PaymentStatusEnum.PENDING.status

        val isOperationUrlNotBlank = invoice.operationUrl.isNotBlank()
        val isOperationUrlBlank = invoice.operationUrl.isBlank()

        if (isPaymentPaid) {
            paidStatusHolder.item_order_status_title_text_view.text = getString(R.string.status_paid)
            paidStatusHolder.item_order_status_date_text_view.text = createdAt.getDayAndMonth()
            paidStatusHolder.show()
        } else if (
            (isPaymentPending && isOperationUrlNotBlank) ||
            (isPaymentNew && isOperationUrlNotBlank)
        ) {
            paidStatusHolder.hide()
            deliveredStatusHolder.hide()
            returnedStatusHolder.hide()
            cancelledStatusHolder.hide()
            notPaidStatusHolder.show()
        } else if (isPaymentPending && isOperationUrlBlank) {
            appliedStatusHolder.apply {
                item_order_status_title_text_view.text = getString(R.string.status_applied)
                item_order_status_date_text_view.text = createdAt.getDayAndMonth()
                show()
            }
        }
    }

    private fun processNotActiveStatus(orderModel: OrderModel) = with (orderModel) {
        val isOrderCompleted = status == OrderStatusEnum.COMPLETED.status
        val isOrderReturned = status == OrderStatusEnum.RETURNED.status
        val isOrderCancelled = status == OrderStatusEnum.CANCELLED.status

        val isDelivered = delivery.deliveryStatus == DeliveryStatusEnum.DELIVERED.status

        if (isOrderCompleted && isDelivered) {
            paidStatusHolder.item_order_status_title_text_view.text = getString(R.string.status_paid)
            paidStatusHolder.item_order_status_date_text_view.text = createdAt.getDayAndMonth()
            paidStatusHolder.item_order_status_to_next_squares_linear_layout.show()
            paidStatusHolder.show()

            deliveredStatusHolder.item_order_status_title_text_view.text = getString(R.string.status_delivered)
            deliveredStatusHolder.item_order_status_date_text_view.text = createdAt.getDayAndMonth()
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
            cancelledStatusHolder.item_order_status_date_text_view.text = createdAt.getDayAndMonth()
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
            returnedStatusHolder.item_order_status_date_text_view.text = createdAt.getDayAndMonth()
            returnedStatusHolder.show()
        }
    }

    private fun navigateToClothes(clothes: ClothesModel) {
        val bundle = Bundle()
        bundle.putInt(ClothesDetailFragment.CLOTHES_ID, clothes.id)

        findNavController().navigate(R.id.action_orderDetailFragment_to_clothesDetailFragment, bundle)
    }
}