package kz.eztech.stylyts.data.api.models.order

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class DeliveryApiModel(
    @SerializedName("city")
    @Expose
    val city: String?,
    @SerializedName("street")
    @Expose
    val street: String?,
    @SerializedName("house")
    @Expose
    val house: String?,
    @SerializedName("apartment")
    @Expose
    val apartment: String?,
    @SerializedName("delivery_type")
    @Expose
    val deliveryType: String?,
    @SerializedName("delivery_status")
    @Expose
    val deliveryStatus: String? = null
)