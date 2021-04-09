package kz.eztech.stylyts.domain.models.clothes

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ClothesCategoryModel(
    @SerializedName("id")
    @Expose
    val id: Int?,
    @SerializedName("clothes_type")
    @Expose
    val clothesType: ClothesTypeModel?,
    @SerializedName("title")
    @Expose
    val title: String?,
    @SerializedName("body_part")
    @Expose
    val bodyPart: Int?
) {
    var isChecked: Boolean = false
}