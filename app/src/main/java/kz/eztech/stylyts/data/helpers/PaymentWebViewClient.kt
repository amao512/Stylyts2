package kz.eztech.stylyts.data.helpers

import android.net.Uri
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import kz.eztech.stylyts.data.api.RestConstants
import kz.eztech.stylyts.presentation.interfaces.PaymentListener

class PaymentWebViewClient(
    private val paymentListener: PaymentListener
) : WebViewClient() {

    override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
        view.loadUrl(request.url.toString())

        if (request.url.equals(Uri.parse(RestConstants.PAYMENT_BACK_URL))) {
            paymentListener.onSuccessNavigate()
        }

        return true
    }

    override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
        view.loadUrl(url)

        if (url == RestConstants.PAYMENT_BACK_URL) {
            paymentListener.onSuccessNavigate()
        }

        return true
    }
}