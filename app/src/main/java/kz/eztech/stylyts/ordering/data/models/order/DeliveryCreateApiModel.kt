package kz.eztech.stylyts.ordering.data.models.order

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class DeliveryCreateApiModel(
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
    val deliveryType: String?
) : Parcelable