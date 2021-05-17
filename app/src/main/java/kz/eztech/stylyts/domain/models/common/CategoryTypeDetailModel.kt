package kz.eztech.stylyts.domain.models.common

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import kz.eztech.stylyts.domain.models.shop.ClothesTypes

/**
 * Created by Ruslan Erdenoff on 18.12.2020.
 */

data class ClothesTypeModel(
    @SerializedName("current_page")
	@Expose
	var current_page: Int? = null,
    @SerializedName("page_size")
	@Expose
	var page_size: Int? = null,
    @SerializedName("total_pages")
	@Expose
	var total_pages: Int? = null,
    @SerializedName("count")
	@Expose
	var count: Int? = null,
    @SerializedName("data")
	@Expose
	var data: List<ClothesTypeDataModel>? = null,
)

@Parcelize
data class ClothesTypeDataModel(
    @SerializedName("id")
	@Expose
	var id: Int? = null,
    @SerializedName("title")
	@Expose
	var title: String? = null,
    @SerializedName("cover_photo")
	@Expose
	var cover_photo: String? = null,
    @SerializedName("cost")
	@Expose
	var cost: Int? = null,
    @SerializedName("sale_cost")
	@Expose
	var sale_cost: Int? = null,
    @SerializedName("currency")
	@Expose
	var currency: String? = null,
    @SerializedName("clothes_type")
	@Expose
	var clothes_types: ClothesTypes? = null,
    @SerializedName("gender")
	@Expose
	var gender: String? = null,
    @SerializedName("constructor_photo")
	@Expose
	var constructor_photo: String? = null,
    @SerializedName("new_arrival")
	@Expose
	var new_arrival: Boolean? = null,

    var isLocated: Boolean = false,
) : Parcelable