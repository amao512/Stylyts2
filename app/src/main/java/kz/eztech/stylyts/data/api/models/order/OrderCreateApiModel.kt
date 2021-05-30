package kz.eztech.stylyts.data.api.models.order

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class OrderCreateApiModel(
    @SerializedName("item_objects")
    @Expose
    val itemObjects: List<Int>?,
    @SerializedName("payment_type")
    @Expose
    val paymentType: String?,
    @SerializedName("customer")
    @Expose
    val customer: CustomerApiModel?,
    @SerializedName("delivery")
    @Expose
    val delivery: DeliveryApiModel?
)