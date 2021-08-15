package kz.eztech.stylyts.global.data.models.clothes

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ClothesCategoryApiModel(
    @SerializedName("id")
    @Expose
    val id: Int?,
    @SerializedName("clothes_type")
    @Expose
    val clothesType: ClothesTypeApiModel?,
    @SerializedName("title")
    @Expose
    val title: String?,
    @SerializedName("body_part")
    @Expose
    val bodyPart: Int?
)