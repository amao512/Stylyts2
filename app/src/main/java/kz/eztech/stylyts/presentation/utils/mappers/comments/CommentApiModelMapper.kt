package kz.eztech.stylyts.presentation.utils.mappers.comments

import kz.eztech.stylyts.data.api.models.comments.CommentApiModel
import kz.eztech.stylyts.domain.models.comments.CommentModel
import kz.eztech.stylyts.presentation.utils.mappers.user.map

fun List<CommentApiModel>?.map(): List<CommentModel> {
    this ?: return emptyList()

    return this.map {
        CommentModel(
            id = it.id ?: 0,
            author = it.author.map(),
            text = it.text.orEmpty(),
            createdAt = it.createdAt.orEmpty(),
            modifiedAt = it.modifiedAt.orEmpty(),
            post = it.post ?: 0
        )
    }
}

fun CommentApiModel?.map(): CommentModel {
    return CommentModel(
        id = this?.id ?: 0,
        author = this?.author.map(),
        text = this?.text.orEmpty(),
        createdAt = this?.createdAt.orEmpty(),
        modifiedAt = this?.modifiedAt.orEmpty(),
        post = this?.post ?: 0
    )
}