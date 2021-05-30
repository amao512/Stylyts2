package kz.eztech.stylyts.data.api.models.order

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class InvoiceApiModel(
    @SerializedName("operation_url")
    @Expose
    val operationUrl: String?,
    @SerializedName("operation_id")
    @Expose
    val operationId: String?,
    @SerializedName("invoice_id")
    @Expose
    val invoiceId: String?
)