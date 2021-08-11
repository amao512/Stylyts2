package kz.eztech.stylyts.presentation.fragments.order_constructor

import android.os.Bundle
import android.webkit.WebView
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.base_toolbar.view.*
import kotlinx.android.synthetic.main.fragment_payment.*
import kz.eztech.stylyts.R
import kz.eztech.stylyts.StylytsApp
import kz.eztech.stylyts.data.helpers.PaymentWebViewClient
import kz.eztech.stylyts.domain.models.order.OrderModel
import kz.eztech.stylyts.presentation.activity.MainActivity
import kz.eztech.stylyts.presentation.base.BaseFragment
import kz.eztech.stylyts.presentation.base.BaseView
import kz.eztech.stylyts.presentation.contracts.ordering.PaymentContract
import kz.eztech.stylyts.presentation.fragments.order.UserOrderDetailFragment
import kz.eztech.stylyts.presentation.interfaces.PaymentListener
import kz.eztech.stylyts.presentation.presenters.ordering.PaymentPresenter
import kz.eztech.stylyts.utils.extensions.show
import javax.inject.Inject

class PaymentFragment : BaseFragment<MainActivity>(), PaymentContract.View, PaymentListener {

    @Inject lateinit var presenter: PaymentPresenter

    private lateinit var webView: WebView

    companion object {
        const val ORDER_ID_KEY = "orderId"
    }

    override fun onResume() {
        super.onResume()
        currentActivity.hideBottomNavigationView()
    }

    override fun getLayoutId(): Int = R.layout.fragment_payment

    override fun getContractView(): BaseView = this

    override fun customizeActionBar() {
        with(fragment_payment_toolbar) {
            toolbar_left_corner_action_image_button.setImageResource(R.drawable.ic_baseline_close_20)
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

    override fun initializeArguments() {}

    override fun initializeViewsData() {

    }

    override fun initializeViews() {
        webView = fragment_payment_web_view
    }

    override fun initializeListeners() {}

    override fun processPostInitialization() {
        presenter.getOrderById(orderId = arguments?.getInt(ORDER_ID_KEY) ?: 0)
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
        webView.settings.javaScriptEnabled = true
        webView.webViewClient = PaymentWebViewClient(paymentListener = this)
        webView.loadUrl(orderModel.invoice.operationUrl)
    }

    override fun onSuccessNavigate() {
        val bundle = Bundle()
        bundle.putInt(UserOrderDetailFragment.ORDER_ID_KEY, arguments?.getInt(ORDER_ID_KEY) ?: 0)

        findNavController().navigate(R.id.action_paymentFragment_to_orderDetailFragment, bundle)
    }
}