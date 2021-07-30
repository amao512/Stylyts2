package kz.eztech.stylyts.data.api.models.referrals

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ReferralItemApiModel(
    @SerializedName("title")
    @Expose
    val title: String?,
    @SerializedName("description")
    @Expose
    val description: String?,
    @SerializedName("cover_images")
    @Expose
    val coverImages: List<String>?,
    @SerializedName("cost")
    @Expose
    val cost: Int?,
    @SerializedName("referral_profit")
    @Expose
    val referralProfit: Int?
)