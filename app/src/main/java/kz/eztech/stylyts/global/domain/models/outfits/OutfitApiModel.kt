package kz.eztech.stylyts.global.domain.models.outfits

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kz.eztech.stylyts.global.data.models.user.UserShortApiModel
import kz.eztech.stylyts.global.data.models.clothes.ClothesApiModel

data class OutfitApiModel(
    @SerializedName("id")
    @Expose
    val id: Int?,
    @SerializedName("clothes")
    @Expose
    val clothes: List<ClothesApiModel>?,
    @SerializedName("author")
    @Expose
    val author: UserShortApiModel?,
    @SerializedName("title")
    @Expose
    val title: String?,
    @SerializedName("text")
    @Expose
    val text: String?,
    @SerializedName("gender")
    @Expose
    val gender: String?,
    @SerializedName("total_price")
    @Expose
    val totalPrice: Int?,
    @SerializedName("currency")
    @Expose
    val currency: String?,
    @SerializedName("cover_photo")
    @Expose
    val coverPhoto: String?,
    @SerializedName("live_photo")
    @Expose
    val livePhoto: String?,
    @SerializedName("clothes_location")
    @Expose
    val clothesLocation: List<ClothesLocationApiModel>?,
    @SerializedName("constructor_code")
    @Expose
    val constructorCode: String?,
    @SerializedName("saved")
    @Expose
    val saved: Boolean,
    @SerializedName("created_at")
    @Expose
    val createdAt: String?,
    @SerializedName("modified_at")
    @Expose
    val modified_at: String?,
    @SerializedName("style")
    @Expose
    val style: Int?,
)