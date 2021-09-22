package kz.eztech.stylyts.global.data.models.posts

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kz.eztech.stylyts.global.data.models.comments.CommentApiModel
import kz.eztech.stylyts.global.data.models.user.UserShortApiModel

data class PostCreateApiModel(
    @SerializedName("id")
    @Expose
    val id: Int?,
    @SerializedName("description")
    @Expose
    val description: String?,
    @SerializedName("author")
    @Expose
    val author: UserShortApiModel?,
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
    val clothes: List<Int>?,
    @SerializedName("comments_count")
    @Expose
    val commentsCount: Int?,
    @SerializedName("likes_count")
    @Expose
    val likesCount: Int?,
    @SerializedName("already_liked")
    @Expose
    val alreadyLiked: Boolean,
    @SerializedName("first_comment")
    @Expose
    val firstComment: CommentApiModel?
)