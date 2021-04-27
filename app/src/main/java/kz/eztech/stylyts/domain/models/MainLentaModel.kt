package kz.eztech.stylyts.domain.models

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import kz.eztech.stylyts.domain.models.shop.ClothesTypes

/**
 * Created by Ruslan Erdenoff on 14.01.2021.
 */

@Parcelize
data class ClothesMainModel(
    @SerializedName("id")
    @Expose
    var id: Int? = null,
    @SerializedName("clothes_type")
    @Expose
    var clothes_type: ClothesTypes? = null,
    @SerializedName("cover_photo")
    @Expose
    var cover_photo: String? = null,
    @SerializedName("constructor_photo")
    @Expose
    var constructor_photo: String? = null,
    @SerializedName("images")
    @Expose
    var images: List<String>? = null,
    @SerializedName("clothes_sizes")
    @Expose
    var clothes_sizes: List<ClothesSize>? = null,
    @SerializedName("clothes_colors")
    @Expose
    var clothes_colors: List<ClothesColor>? = null,
    @SerializedName("brand")
    @Expose
    var brand: BrandMainModel? = null,
    @SerializedName("title")
    @Expose
    var title: String? = null,
    @SerializedName("description")
    @Expose
    var description: String? = null,
    @SerializedName("gender")
    @Expose
    var gender: String? = null,
    @SerializedName("cost")
    @Expose
    var cost: Int? = null,
    @SerializedName("sale_cost")
    @Expose
    var sale_cost: Int? = null,
    @SerializedName("currency")
    @Expose
    var currency: String? = null,
    @SerializedName("product_code")
    @Expose
    var product_code: String? = null,

    var currentColor: ClothesColor? = null,
    var currentSize: ClothesSize? = null,
    var clothe_location: ClothesLocation? = null,
) : Parcelable

@Parcelize
data class ClothesLocation(
    @SerializedName("clothes_id")
    @Expose
    var clothes_id: Int? = null,
    @SerializedName("point_x")
    @Expose
    var point_x: Double? = null,
    @SerializedName("point_y")
    @Expose
    var point_y: Double? = null,
    @SerializedName("width")
    @Expose
    var width: Double? = null,
    @SerializedName("height")
    @Expose
    var height: Double? = null,
    @SerializedName("degree")
    @Expose
    var degree: Double? = null,
) : Parcelable

@Parcelize
data class ClothesSize(
    @SerializedName("id")
    @Expose
    var id: Int? = null,
    @SerializedName("size")
    @Expose
    var size: String? = null
) : Parcelable

@Parcelize
data class ClothesColor(
    @SerializedName("id")
    @Expose
    var id: Int? = null,
    @SerializedName("color")
    @Expose
    var color: String? = null
) : Parcelable

@Parcelize
data class BrandMainModel(
    @SerializedName("id")
    @Expose
    var id: Int? = null,
    @SerializedName("is_brand")
    @Expose
    var is_brand: Boolean? = null,
    @SerializedName("role")
    @Expose
    var role: String? = null,
    @SerializedName("first_name")
    @Expose
    var first_name: String? = null,
    @SerializedName("last_name")
    @Expose
    var last_name: String? = null,
    @SerializedName("avatar")
    @Expose
    var avatar: String? = null
) : Parcelable