package kz.eztech.stylyts.data.api.models.posts

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kz.eztech.stylyts.data.api.models.clothes.ClothesApiModel

data class PostApiModel(
    @SerializedName("id")
    @Expose
    val id: Int?,
    @SerializedName("description")
    @Expose
    val description: String?,
    @SerializedName("author")
    @Expose
    val author: Int?,
    @SerializedName("images")
    @Expose
    val images: List<String>?,
    @SerializedName("tags")
    @Expose
    val tags: TagsApiModel?,
    @SerializedName("hidden")
    @Expose
    val hidden: Boolean,
    @SerializedName("clothes")
    @Expose
    val clothes: List<ClothesApiModel>?
)