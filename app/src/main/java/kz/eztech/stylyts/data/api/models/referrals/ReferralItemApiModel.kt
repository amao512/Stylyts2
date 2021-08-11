package kz.eztech.stylyts.data.api.models.referrals

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kz.eztech.stylyts.data.api.models.clothes.ClothesBrandApiModel

data class ReferralItemApiModel(
    @SerializedName("id")
    @Expose
    val id: Int?,
    @SerializedName("title")
    @Expose
    val title: String?,
    @SerializedName("description")
    @Expose
    val description: String?,
    @SerializedName("cover_images")
    @Expose
    val coverImages: List<String>?,
    @SerializedName("clothes_brand")
    @Expose
    val clothesBrand: ClothesBrandApiModel?,
    @SerializedName("cost")
    @Expose
    val cost: Int?,
    @SerializedName("count")
    @Expose
    val count: Int?,
    @SerializedName("referral_profit")
    @Expose
    val referralProfit: Int?
)