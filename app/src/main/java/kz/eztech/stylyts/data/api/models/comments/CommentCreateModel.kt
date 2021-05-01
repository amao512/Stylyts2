package kz.eztech.stylyts.data.api.models.comments

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class CommentCreateModel(
    @SerializedName("text")
    @Expose
    val text: String?,
    @SerializedName("post")
    @Expose
    val postId: Int?
)