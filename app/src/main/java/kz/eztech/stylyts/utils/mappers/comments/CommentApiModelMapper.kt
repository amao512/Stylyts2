package kz.eztech.stylyts.utils.mappers.comments

import kz.eztech.stylyts.data.api.models.comments.CommentApiModel
import kz.eztech.stylyts.domain.models.comments.CommentModel
import kz.eztech.stylyts.utils.extensions.getZonedDateTime
import kz.eztech.stylyts.utils.mappers.user.map

fun List<CommentApiModel>?.map(): List<CommentModel> {
    this ?: return emptyList()

    return this.map {
        CommentModel(
            id = it.id ?: 0,
            author = it.author.map(),
            text = it.text.orEmpty(),
            createdAt = it.createdAt.orEmpty().getZonedDateTime(),
            modifiedAt = it.modifiedAt.orEmpty().getZonedDateTime(),
            post = it.post ?: 0
        )
    }
}

fun CommentApiModel?.map(): CommentModel {
    return CommentModel(
        id = this?.id ?: 0,
        author = this?.author.map(),
        text = this?.text.orEmpty(),
        createdAt = this?.createdAt.orEmpty().getZonedDateTime(),
        modifiedAt = this?.modifiedAt.orEmpty().getZonedDateTime(),
        post = this?.post ?: 0
    )
}