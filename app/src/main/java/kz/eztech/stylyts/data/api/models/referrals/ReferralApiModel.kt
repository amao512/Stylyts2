package kz.eztech.stylyts.data.api.models.referrals

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kz.eztech.stylyts.data.api.models.user.UserShortApiModel

data class ReferralApiModel(
    @SerializedName("id")
    @Expose
    val id: Int?,
    @SerializedName("buyer")
    @Expose
    val buyer: UserShortApiModel?,
    @SerializedName("approved")
    @Expose
    val approved: Boolean,
    @SerializedName("referral_cost")
    @Expose
    val referralCost: Int?,
    @SerializedName("created_at")
    @Expose
    val createdAt: String?,
    @SerializedName("order")
    @Expose
    val order: Int?
)