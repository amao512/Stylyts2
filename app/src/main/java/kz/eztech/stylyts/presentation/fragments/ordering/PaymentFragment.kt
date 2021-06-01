package kz.eztech.stylyts.presentation.fragments.ordering

import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.base_toolbar.view.*
import kotlinx.android.synthetic.main.fragment_payment.*
import kz.eztech.stylyts.R
import kz.eztech.stylyts.StylytsApp
import kz.eztech.stylyts.domain.models.order.OrderModel
import kz.eztech.stylyts.presentation.activity.MainActivity
import kz.eztech.stylyts.presentation.base.BaseFragment
import kz.eztech.stylyts.presentation.base.BaseView
import kz.eztech.stylyts.presentation.contracts.ordering.PaymentContract
import kz.eztech.stylyts.presentation.presenters.ordering.PaymentPresenter
import kz.eztech.stylyts.presentation.utils.extensions.show
import javax.inject.Inject

class PaymentFragment : BaseFragment<MainActivity>(), PaymentContract.View, View.OnClickListener {

    @Inject lateinit var presenter: PaymentPresenter

    private lateinit var webView: WebView

    companion object {
        const val ORDER_ID_KEY = "orderId"
    }

    override fun getLayoutId(): Int = R.layout.fragment_payment

    override fun getContractView(): BaseView = this

    override fun customizeActionBar() {
        with (fragment_payment_toolbar) {
            toolbar_left_corner_action_image_button.setImageResource(R.drawable.ic_close)
            toolbar_left_corner_action_image_button.setOnClickListener(this@PaymentFragment)
            toolbar_left_corner_action_image_button.show()

            base_toolbar_small_title_text_view.text = "Оплата"
            base_toolbar_title_with_subtitle.show()
        }
    }

    override fun initializeDependency() {
        (currentActivity.application as StylytsApp).applicationComponent.inject(fragment = this)
    }

    override fun initializePresenter() {
        presenter.attach(view = this)
    }

    override fun initializeArguments() {}

    override fun initializeViewsData() {}

    override fun initializeViews() {
        webView = fragment_payment_web_view
    }

    override fun initializeListeners() {}

    override fun processPostInitialization() {
        presenter.getOrderById(
            token = currentActivity.getTokenFromSharedPref(),
            orderId = arguments?.getInt(ORDER_ID_KEY) ?: 0
        )
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

    override fun processOrder(orderModel: OrderModel) {
        fragment_payment_toolbar.base_toolbar_small_title_sub_text_view.text = orderModel.invoice.operationUrl

        webView.webViewClient = WebViewClient()
        webView.loadUrl(orderModel.invoice.operationUrl)
    }
}