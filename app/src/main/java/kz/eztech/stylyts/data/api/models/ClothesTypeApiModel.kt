package kz.eztech.stylyts.data.api.models

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
    val womenCoverPhoto: String?
)