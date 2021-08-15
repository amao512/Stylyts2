package kz.eztech.stylyts.ordering.data.models.referrals

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kz.eztech.stylyts.global.data.models.user.UserShortApiModel

data class ReferralApiModel(
    @SerializedName("id")
    @Expose
    val id: Int?,
    @SerializedName("buyer")
    @Expose
    val buyer: UserShortApiModel?,
    @SerializedName("items")
    @Expose
    val items: List<ReferralItemApiModel>?,
    @SerializedName("approved")
    @Expose
    val approved: Boolean,
    @SerializedName("total_profit")
    @Expose
    val totalProfit: Int?,
    @SerializedName("referral_percentage")
    @Expose
    val referralPercentage: Int?,
    @SerializedName("created_at")
    @Expose
    val createdAt: String?,
    @SerializedName("order")
    @Expose
    val order: Int?
)