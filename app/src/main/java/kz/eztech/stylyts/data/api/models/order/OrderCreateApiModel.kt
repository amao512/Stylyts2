package kz.eztech.stylyts.data.api.models.order

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class OrderCreateApiModel(
    @SerializedName("id")
    @Expose
    val id: Int? = null,
    @SerializedName("invoice")
    @Expose
    val invoice: InvoiceApiModel? = null,
    @SerializedName("item_objects")
    @Expose
    var itemObjects: List<Int>,
    @SerializedName("payment_type")
    @Expose
    var paymentType: String?,
    @SerializedName("customer")
    @Expose
    val customer: CustomerApiModel?,
    @SerializedName("delivery")
    @Expose
    val delivery: DeliveryCreateApiModel?,
) {
    var ownerId: Int = 0
    var created = false
}