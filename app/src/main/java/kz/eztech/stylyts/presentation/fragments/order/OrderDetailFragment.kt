package kz.eztech.stylyts.presentation.fragments.order

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.base_toolbar.view.*
import kotlinx.android.synthetic.main.fragment_order_detail.*
import kz.eztech.stylyts.R
import kz.eztech.stylyts.StylytsApp
import kz.eztech.stylyts.domain.models.order.OrderModel
import kz.eztech.stylyts.presentation.activity.MainActivity
import kz.eztech.stylyts.presentation.adapters.ordering.OrderAdapter
import kz.eztech.stylyts.presentation.base.BaseFragment
import kz.eztech.stylyts.presentation.base.BaseView
import kz.eztech.stylyts.presentation.contracts.ordering.OrderDetailContract
import kz.eztech.stylyts.presentation.fragments.order_constructor.PaymentFragment
import kz.eztech.stylyts.presentation.presenters.ordering.OrderDetailPresenter
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
    private lateinit var addressTextView: TextView

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
        addressTextView = fragment_order_detail_address_text_view
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

        payButton.setOnClickListener {
            val bundle = Bundle()
            bundle.putInt(PaymentFragment.ORDER_ID_KEY, orderModel.id)

            findNavController().navigate(R.id.action_orderDetailFragment_to_paymentFragment, bundle)
        }

        addressTextView.text = getString(
            R.string.address_detail_text_format,
            orderModel.delivery.city,
            orderModel.delivery.street,
            orderModel.delivery.house,
            orderModel.delivery.apartment
        )
    }
}