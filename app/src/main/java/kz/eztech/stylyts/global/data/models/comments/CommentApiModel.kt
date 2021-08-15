package kz.eztech.stylyts.global.data.models.comments

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kz.eztech.stylyts.global.data.models.user.UserShortApiModel

data class CommentApiModel(
    @SerializedName("id")
    @Expose
    val id: Int?,
    @SerializedName("author")
    @Expose
    val author: UserShortApiModel?,
    @SerializedName("text")
    @Expose
    val text: String?,
    @SerializedName("created_at")
    @Expose
    val createdAt: String?,
    @SerializedName("modified_at")
    @Expose
    val modifiedAt: String?,
    @SerializedName("post")
    @Expose
    val post: Int?
)