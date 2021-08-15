package kz.eztech.stylyts.ordering.data.webViewClient

import android.net.Uri
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import kz.eztech.stylyts.global.data.constants.RestConstants
import kz.eztech.stylyts.ordering.presentation.order_constructor.listeners.PaymentListener

class PaymentWebViewClient(
    private val paymentListener: PaymentListener
) : WebViewClient() {

    private val testUrl = "https://www.stylyts.com/"

    override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
        view.loadUrl(request.url.toString())

        if (request.url.equals(Uri.parse(RestConstants.PAYMENT_BACK_URL)) || request.url.equals(Uri.parse(testUrl))) {
            paymentListener.onSuccessNavigate()
        }

        return true
    }

    override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
        view.loadUrl(url)

        if (url == RestConstants.PAYMENT_BACK_URL || url == testUrl) {
            paymentListener.onSuccessNavigate()
        }

        return true
    }
}