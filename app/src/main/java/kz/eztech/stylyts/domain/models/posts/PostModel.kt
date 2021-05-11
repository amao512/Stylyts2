package kz.eztech.stylyts.domain.models.posts

import kz.eztech.stylyts.domain.models.clothes.ClothesModel
import kz.eztech.stylyts.domain.models.comments.CommentModel
import kz.eztech.stylyts.domain.models.user.UserShortModel

data class PostModel(
    val id: Int,
    val description: String,
    val author: UserShortModel,
    val images: List<String>,
    val tags: TagsModel,
    val hidden: Boolean,
    val clothes: List<ClothesModel>,
    val commentsCount: Int,
    val firstComment: CommentModel,
    var likesCount: Int,
    var alreadyLiked: Boolean
)