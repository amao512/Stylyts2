package kz.eztech.stylyts.domain.models.comments

import kz.eztech.stylyts.domain.models.user.UserShortModel

class CommentModel(
    val id: Int,
    val author: UserShortModel,
    val text: String,
    val createdAt: String,
    val modifiedAt: String,
    val post: Int
)