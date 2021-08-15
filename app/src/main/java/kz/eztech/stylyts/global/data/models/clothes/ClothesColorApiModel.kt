package kz.eztech.stylyts.global.data.models.clothes

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ClothesColorApiModel(
    @SerializedName("id")
    @Expose
    val id: Int?,
    @SerializedName("title")
    @Expose
    val title: String?,
    @SerializedName("color")
    @Expose
    val color: String?
)