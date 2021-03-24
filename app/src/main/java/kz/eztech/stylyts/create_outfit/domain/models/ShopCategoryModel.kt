package kz.eztech.stylyts.create_outfit.domain.models

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

/**
 * Created by Ruslan Erdenoff on 26.11.2020.
 */
data class ShopCategoryModel(
    @SerializedName("M")
    @Expose
    val menCategory: ArrayList<GenderCategory>? = null,
    @SerializedName("F")
    @Expose
    val femaleCategory: ArrayList<GenderCategory>? = null
)

@Parcelize
data class GenderCategory(
    @SerializedName("id")
    @Expose
    var id: Int? = null,
    @SerializedName("clothes_types")
    @Expose
    var clothes_types: List<ClothesTypes>? = null,
    @SerializedName("cover_image")
    @Expose
    var cover_image: String? = null,
    @SerializedName("constructor_icon")
    @Expose
    var constructor_icon: String? = null,
    @SerializedName("title")
    @Expose
    var title: String? = null,
    var isExternal: Boolean = false,
    var externalTitle: String? = null,
    var externalImageId: Int = 0,
    var externalType: Int = 0,
    var isChoosen: Boolean = false,
    var chosenClothesTypes: Int? = null
) : Parcelable

@Parcelize
data class ClothesTypes(
    @SerializedName("id")
    @Expose
    var id: Int? = null,
    @SerializedName("title")
    @Expose
    var title: String? = null,
    @SerializedName("clothes_category")
    @Expose
    var clothes_category: Int? = null,
    @SerializedName("body_part")
    @Expose
    var body_part: Int? = null,
    var constructor_icon: String? = null,
    var isChosen: Boolean = false
) : Parcelable