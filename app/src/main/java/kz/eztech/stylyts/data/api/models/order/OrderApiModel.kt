package kz.eztech.stylyts.data.api.models.order

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kz.eztech.stylyts.data.api.models.user.UserShortApiModel

data class OrderApiModel(
    @SerializedName("id")
    @Expose
    val id: Int?,
    @SerializedName("invoice")
    @Expose
    val invoice: InvoiceApiModel?,
    @SerializedName("price")
    @Expose
    val price: Int?,
    @SerializedName("seller")
    @Expose
    val seller: UserShortApiModel?,
    @SerializedName("client")
    @Expose
    val client: UserShortApiModel?,
    @SerializedName("item_objects")
    @Expose
    val itemObject: List<Int>?,
    @SerializedName("delivery")
    @Expose
    val delivery: DeliveryApiModel?,
    @SerializedName("customer")
    @Expose
    val customer: CustomerApiModel?,
    @SerializedName("status")
    @Expose
    val status: String?,
    @SerializedName("item_titles")
    @Expose
    val itemTitles: List<String>?,
    @SerializedName("created_at")
    @Expose
    val createdAt: String?,
    @SerializedName("modified_at")
    @Expose
    val modifiedAt: String?
)