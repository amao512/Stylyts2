package kz.eztech.stylyts.presentation.fragments.order_constructor

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.base_toolbar.view.*
import kotlinx.android.synthetic.main.fragment_ordering.*
import kz.eztech.stylyts.R
import kz.eztech.stylyts.StylytsApp
import kz.eztech.stylyts.data.api.models.order.OrderCreateApiModel
import kz.eztech.stylyts.data.db.cart.CartEntity
import kz.eztech.stylyts.domain.models.order.OrderCreateModel
import kz.eztech.stylyts.presentation.activity.MainActivity
import kz.eztech.stylyts.presentation.base.BaseFragment
import kz.eztech.stylyts.presentation.base.BaseView
import kz.eztech.stylyts.presentation.contracts.ordering.OrderingContract
import kz.eztech.stylyts.presentation.enums.ordering.PaymentTypeEnum
import kz.eztech.stylyts.presentation.fragments.card.CardFragment
import kz.eztech.stylyts.presentation.presenters.ordering.OrderingPresenter
import kz.eztech.stylyts.presentation.utils.extensions.hide
import kz.eztech.stylyts.presentation.utils.extensions.show
import java.text.NumberFormat
import javax.inject.Inject

class OrderingFragment : BaseFragment<MainActivity>(), OrderingContract.View, View.OnClickListener {

    @Inject lateinit var presenter: OrderingPresenter

    private lateinit var paymentCashLinearLayout: LinearLayout
    private lateinit var paymentCardLinearLayout: LinearLayout
    private lateinit var goodsCountTextView: TextView
    private lateinit var goodsPriceSumTextView: TextView
    private lateinit var deliveryPriceTextView: TextView
    private lateinit var discountPriceTextView: TextView
    private lateinit var totalPriceTextView: TextView
    private lateinit var completeButton: Button

    private var paymentType = CASH_PAYMENT
    private var orderList = ArrayList<OrderCreateApiModel>()
    private var createdOrderList: MutableList<OrderCreateModel> = mutableListOf()

    companion object {
        private const val CASH_PAYMENT = 1
        private const val CARD_PAYMENT = 2

        const val ORDER_KEY = "order"
    }

    override fun getLayoutId(): Int = R.layout.fragment_ordering

    override fun getContractView(): BaseView = this

    override fun customizeActionBar() {
        with(fragment_ordering_toolbar) {
            toolbar_left_corner_action_image_button.setImageResource(R.drawable.ic_baseline_keyboard_arrow_left_24)
            toolbar_left_corner_action_image_button.show()
            toolbar_title_text_view.show()

            customizeActionToolBar(toolbar = this, title = getString(R.string.button_ordering))
        }
    }

    override fun initializeDependency() {
        (currentActivity.application as StylytsApp).applicationComponent.inject(fragment = this)
    }

    override fun initializePresenter() {
        presenter.attach(view = this)
    }

    override fun initializeArguments() {
        arguments?.let {
            if (it.containsKey(ORDER_KEY)) {
                orderList.clear()
                it.getParcelableArrayList<OrderCreateApiModel>(ORDER_KEY)?.map { order ->
                    orderList.add(order)
                }
            }
        }
    }

    override fun initializeViewsData() {
        findNavController().currentBackStackEntry?.savedStateHandle
            ?.getLiveData<Int>(CardFragment.CARD_KEY)
            ?.observe(viewLifecycleOwner, {
                setPaymentType(CARD_PAYMENT)
            })
    }

    override fun initializeViews() {
        paymentCashLinearLayout = fragment_ordering_payment_cash_linear_layout
        paymentCardLinearLayout = fragment_ordering_payment_card_linear_layout
        goodsCountTextView = fragment_ordering_goods_count_text_view
        goodsPriceSumTextView = fragment_ordering_price_sum_text_view
        deliveryPriceTextView = fragment_ordering_delivery_price_text_view
        discountPriceTextView = fragment_ordering_discount_price_text_view
        totalPriceTextView = fragment_ordering_total_price_text_view
        completeButton = fragment_ordering_complete_button
    }

    override fun initializeListeners() {
        paymentCashLinearLayout.setOnClickListener(this)
        paymentCardLinearLayout.setOnClickListener(this)
        completeButton.setOnClickListener(this)
    }

    override fun processPostInitialization() {
        presenter.getGoodsFromCart()
    }

    override fun disposeRequests() {
        presenter.disposeRequests()
    }

    override fun displayMessage(msg: String) {
        displayToast(msg)
    }

    override fun isFragmentVisible(): Boolean = isVisible

    override fun displayProgress() {
        fragment_ordering_progress_bar.show()
        fragment_ordering_complete_button.isClickable = false
        fragment_ordering_toolbar.toolbar_left_corner_action_image_button.isClickable = false
    }

    override fun hideProgress() {
        fragment_ordering_progress_bar.hide()
        fragment_ordering_complete_button.isClickable = true
        fragment_ordering_toolbar.toolbar_left_corner_action_image_button.isClickable = true
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.fragment_ordering_payment_cash_linear_layout -> setPaymentType(CASH_PAYMENT)
            R.id.fragment_ordering_payment_card_linear_layout -> navigateToSaveCardFragment()
            R.id.fragment_ordering_complete_button -> onCompleteButtonClick()
        }
    }

    override fun processGoods(list: List<CartEntity>) {
        goodsCountTextView.text = list.sumBy { it.count!! }.toString()
        goodsPriceSumTextView.text = NumberFormat.getInstance().format(list.sumBy { it.price!! })
        deliveryPriceTextView.text = "0 тг"
        discountPriceTextView.text = getSalePrice(list)
        totalPriceTextView.text = getTotalPrice(list)
        completeButton.text = getString(R.string.ordering_button_text_format, getTotalPrice(list))
    }

    override fun processSuccessCreating(orderModel: OrderCreateModel) {
        createdOrderList.add(orderModel)

        if (createdOrderList.size == orderList.size) {
            hideProgress()

            if (createdOrderList.size == 1 && orderModel.invoice.operationUrl.isNotBlank()) {
                val bundle = Bundle()
                bundle.putInt(PaymentFragment.ORDER_ID_KEY, orderModel.id)

                findNavController().navigate(R.id.action_orderingFragment_to_paymentFragment, bundle)
            } else {
                findNavController().navigate(R.id.action_orderingFragment_to_orderListFragment)
            }
        }
    }

    private fun getSalePrice(list: List<CartEntity>): String {
        var salePrice = 0
        list.map {
            if (it.salePrice != 0) {
                salePrice += it.price?.minus(it.salePrice!!) ?: 0
            }
        }

        return getString(
            R.string.price_tenge_text_format,
            if (salePrice != 0) {
                "-${NumberFormat.getInstance().format(salePrice)}"
            } else {
                NumberFormat.getInstance().format(salePrice)
            }
        )
    }

    private fun getTotalPrice(list: List<CartEntity>): String {
        return getString(
            R.string.price_tenge_text_format, NumberFormat.getInstance().format(
                list.sumBy {
                    if (it.salePrice != 0) {
                        it.salePrice!!
                    } else {
                        it.price!!
                    }
                }
            )
        )
    }

    private fun setPaymentType(type: Int) {
        paymentType = type

        when (paymentType) {
            CASH_PAYMENT -> {
                fragment_ordering_payment_cash_selected_image_view.show()
                fragment_ordering_payment_card_selected_image_view.hide()
            }
            CARD_PAYMENT -> {
                fragment_ordering_payment_cash_selected_image_view.hide()
                fragment_ordering_payment_card_selected_image_view.show()
            }
        }
    }

    private fun navigateToSaveCardFragment() {
        setPaymentType(type = CARD_PAYMENT)
//        val bundle = Bundle()
//        bundle.putInt(CardFragment.MODE_KEY, CardFragment.GET_CARD_MODE)
//
//        findNavController().navigate(R.id.action_orderingFragment_to_cardFragment, bundle)
    }

    private fun onCompleteButtonClick() {
        orderList.map { order ->
            when (paymentType) {
                CASH_PAYMENT -> order.paymentType = PaymentTypeEnum.CASH.type
                CARD_PAYMENT -> order.paymentType = PaymentTypeEnum.CARD.type
            }
        }

        presenter.createOrders(orderList = orderList)
    }
}