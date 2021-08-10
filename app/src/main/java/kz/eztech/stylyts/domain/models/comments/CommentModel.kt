package kz.eztech.stylyts.domain.models.comments

import kz.eztech.stylyts.domain.models.user.UserShortModel
import org.threeten.bp.ZonedDateTime

class CommentModel(
    val id: Int,
    val author: UserShortModel,
    val text: String,
    val createdAt: ZonedDateTime,
    val modifiedAt: ZonedDateTime,
    val post: Int
)