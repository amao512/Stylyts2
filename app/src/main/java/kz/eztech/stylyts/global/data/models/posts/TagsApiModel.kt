package kz.eztech.stylyts.global.data.models.posts

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class TagsApiModel(
    @SerializedName("clothes_tags")
    @Expose
    val clothesTags: List<TagApiModel>?,
    @SerializedName("users_tags")
    @Expose
    val usersTags: List<TagApiModel>?
)