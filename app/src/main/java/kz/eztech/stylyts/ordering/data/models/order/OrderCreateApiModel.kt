package kz.eztech.stylyts.ordering.data.models.order

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import kz.eztech.stylyts.BuildConfig

@Parcelize
data class OrderCreateApiModel(
    @SerializedName("id")
    @Expose
    val id: Int? = null,
    @SerializedName("invoice")
    @Expose
    val invoice: InvoiceApiModel? = null,
    @SerializedName("items_meta_data")
    @Expose
    var itemsMetaData: List<OrderItemApiModel>? = null,
    @SerializedName("payment_type")
    @Expose
    var paymentType: String?,
    @SerializedName("customer")
    @Expose
    val customer: CustomerApiModel?,
    @SerializedName("delivery")
    @Expose
    var delivery: DeliveryCreateApiModel? = null,
    @SerializedName("back_url")
    @Expose
    val backUrl: String? = BuildConfig.PAYMENT_BACK_URL
) : Parcelable {
    var ownerId: Int = 0
    var created = false
    var orderItemList: MutableList<OrderItemApiModel> = mutableListOf()
}

data class ResponseOrderCreateApiModel(
    @SerializedName("id")
    @Expose
    val id: Int?,
    @SerializedName("invoice")
    @Expose
    val invoice: InvoiceApiModel?,
    @SerializedName("item_objects")
    @Expose
    val itemObjects: List<Int>?
)