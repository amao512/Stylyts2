package kz.eztech.stylyts.ordering.data.models.order

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class InvoiceApiModel(
    @SerializedName("operation_url")
    @Expose
    val operationUrl: String?,
    @SerializedName("operation_id")
    @Expose
    val operationId: String?,
    @SerializedName("invoice_id")
    @Expose
    val invoiceId: String?,
    @SerializedName("payment_status")
    @Expose
    val paymentStatus: String?
) : Parcelable