package kz.eztech.stylyts.global.data.models.posts

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class TagApiModel(
    @SerializedName("id")
    @Expose
    val id: Int?,
    @SerializedName("title")
    @Expose
    val title: String?,
    @SerializedName("point_x")
    @Expose
    val pointX: Double?,
    @SerializedName("point_y")
    @Expose
    val pointY: Double?
)
