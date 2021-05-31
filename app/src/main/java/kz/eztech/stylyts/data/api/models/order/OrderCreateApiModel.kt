package kz.eztech.stylyts.data.api.models.order

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class OrderCreateApiModel(
    @SerializedName("item_objects")
    @Expose
    var itemObjects: List<Int>?,
    @SerializedName("payment_type")
    @Expose
    var paymentType: String?,
    @SerializedName("customer")
    @Expose
    val customer: CustomerApiModel?,
    @SerializedName("delivery")
    @Expose
    val delivery: DeliveryApiModel?
) : Parcelable