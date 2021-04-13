package kz.eztech.stylyts.data.api.models.outfits

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ClothesLocationApiModel(
    @SerializedName("clothes_id")
    @Expose
    var clothesId: Int?,
    @SerializedName("point_x")
    @Expose
    var pointX: Double?,
    @SerializedName("point_y")
    @Expose
    var pointY: Double?,
    @SerializedName("width")
    @Expose
    var width: Double?,
    @SerializedName("height")
    @Expose
    var height: Double?,
    @SerializedName("degree")
    @Expose
    var degree: Double?,
)