package kz.eztech.stylyts.data.api.models.clothes

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kz.eztech.stylyts.data.api.models.OwnerApiModel

data class ClothesApiModel(
    @SerializedName("id")
    @Expose
    val id: Int?,
    @SerializedName("clothes_style")
    @Expose
    val clothesStyle: ClothesStyleApiModel?,
    @SerializedName("clothes_category")
    @Expose
    val clothesCategory: ClothesCategoryApiModel?,
    @SerializedName("constructor_image")
    @Expose
    val constructorImage: String?,
    @SerializedName("cover_images")
    @Expose
    val coverImages: List<String>?,
    @SerializedName("clothes_sizes")
    @Expose
    val clothesSizes: List<String>?,
    @SerializedName("clothes_colors")
    @Expose
    val clothesColors: List<String>?,
    @SerializedName("title")
    @Expose
    val title: String?,
    @SerializedName("description")
    @Expose
    val description: String?,
    @SerializedName("gender")
    @Expose
    val gender: String?,
    @SerializedName("cost")
    @Expose
    val cost: Int?,
    @SerializedName("sale_cost")
    @Expose
    val saleCost: Int?,
    @SerializedName("currency")
    @Expose
    val currency: String?,
    @SerializedName("product_code")
    @Expose
    val productCode: String?,
    @SerializedName("created_at")
    @Expose
    val createdAt: String?,
    @SerializedName("modified_at")
    @Expose
    val modifiedAt: String?,
    @SerializedName("owner")
    @Expose
    val owner: OwnerApiModel?,
    @SerializedName("clothes_brand")
    @Expose
    val clothesBrand: ClothesBrandApiModel?,
)