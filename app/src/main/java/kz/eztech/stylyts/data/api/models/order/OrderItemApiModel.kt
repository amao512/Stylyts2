package kz.eztech.stylyts.data.api.models.order

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
open class BaseOrderItemModel : Parcelable

@Parcelize
data class OrderItemApiModel(
    @SerializedName("count")
    @Expose
    val count: Int?,
    @SerializedName("size")
    @Expose
    val size: String?,
    @SerializedName("clothes")
    @Expose
    val clothes: Int?,
    @SerializedName("referral_user")
    @Expose
    val referralUser: Int?
) : BaseOrderItemModel()

@Parcelize
data class OrderItemWithoutReferralApiModel(
    @SerializedName("count")
    @Expose
    val count: Int?,
    @SerializedName("size")
    @Expose
    val size: String?,
    @SerializedName("clothes")
    @Expose
    val clothes: Int?
) : BaseOrderItemModel()