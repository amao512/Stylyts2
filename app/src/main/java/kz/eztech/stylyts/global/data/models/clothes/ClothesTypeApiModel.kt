package kz.eztech.stylyts.global.data.models.clothes

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ClothesTypeApiModel(
    @SerializedName("id")
    @Expose
    val id: Int?,
    @SerializedName("title")
    @Expose
    val title: String?,
    @SerializedName("men_cover_photo")
    @Expose
    val menCoverPhoto: String?,
    @SerializedName("women_cover_photo")
    @Expose
    val womenCoverPhoto: String?,
    @SerializedName("men_constructor_photo")
    @Expose
    val menConstructorPhoto: String?,
    @SerializedName("women_constructor_photo")
    @Expose
    val womenConstructorPhoto: String?
)