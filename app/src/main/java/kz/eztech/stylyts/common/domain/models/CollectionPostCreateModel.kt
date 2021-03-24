package kz.eztech.stylyts.common.domain.models

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import okhttp3.MultipartBody

/**
 * Created by Ruslan Erdenoff on 25.12.2020.
 */
/* "title": "My new outlook",
    "clothes": [
        3
    ],
    "clothes_location": [
        {
        	"clothes_id": 3,
        	"point_x": 123.33,
        	"point_y": 321.33,
        	"width": 100.00,
        	"height": 101.10,
        	"degree": 99.55
        },
        {
        	"clothes_id": 3,
        	"point_x": 123.33,
        	"point_y": 321.33,
        	"width": 100.00,
        	"height": 101.10,
        	"degree": 99.55
        },
        {
        	"clothes_id": 3,
        	"point_x": 123.33,
        	"point_y": 321.33,
        	"width": 100.00,
        	"height": 101.10,
        	"degree": 99.55
        }
    ],
    "style": 1,
    "total_price": 1.1,
    "author": 1,
    "text": "thisisrandomhastag"
* */
@Parcelize
data class CollectionPostCreateModel(
    @SerializedName("title")
    @Expose
    var title: String? = null,
    @SerializedName("clothes")
    @Expose
    var clothes: List<Int>? = null,
    @SerializedName("clothes_location")
    @Expose
    var clothes_location: List<ClothesCollection>? = null,
    @SerializedName("style")
    @Expose
    var style: Int? = null,
    @SerializedName("author")
    @Expose
    var author: Int? = null,
    @SerializedName("total_price")
    @Expose
    var total_price: Float? = null,
    @SerializedName("text")
    @Expose
    var text: String? = null
) : Parcelable

@Parcelize
data class ClothesCollection(
    @SerializedName("clothes_id")
    @Expose
    var clothes_id: Int? = null,
    @SerializedName("point_x")
    @Expose
    var point_x: Float? = null,
    @SerializedName("point_y")
    @Expose
    var point_y: Float? = null,
    @SerializedName("width")
    @Expose
    var width: Float? = null,
    @SerializedName("height")
    @Expose
    var height: Float? = null,
    @SerializedName("degree")
    @Expose
    var degree: Float? = null,
) : Parcelable

data class ExportedCollectionPostCreateModel(
    @SerializedName("title")
    @Expose
    var title: String? = null,
    @SerializedName("clothes")
    @Expose
    var clothes: List<Int>? = null,
    @SerializedName("clothes_location")
    @Expose
    var clothes_location: List<ClothesCollection>? = null,
    @SerializedName("style")
    @Expose
    var style: Int? = null,
    @SerializedName("author")
    @Expose
    var author: Int? = null,
    @SerializedName("total_price")
    @Expose
    var total_price: Float? = null,
    @SerializedName("text")
    @Expose
    var text: String? = null,
    @SerializedName("cover_image")
    var cover_image: MultipartBody.Part
)
