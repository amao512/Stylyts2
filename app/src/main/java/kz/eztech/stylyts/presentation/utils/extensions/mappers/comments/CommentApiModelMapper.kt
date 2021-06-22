package kz.eztech.stylyts.presentation.utils.extensions.mappers.comments

import kz.eztech.stylyts.data.api.models.comments.CommentApiModel
import kz.eztech.stylyts.presentation.utils.extensions.mappers.user.map
import kz.eztech.stylyts.domain.models.comments.CommentModel
import kz.eztech.stylyts.presentation.utils.EMPTY_STRING

fun List<CommentApiModel>?.map(): List<CommentModel> {
    this ?: return emptyList()

    return this.map {
        CommentModel(
            id = it.id ?: 0,
            author = it.author.map(),
            text = it.text ?: EMPTY_STRING,
            createdAt = it.createdAt ?: EMPTY_STRING,
            modifiedAt = it.modifiedAt ?: EMPTY_STRING,
            post = it.post ?: 0
        )
    }
}

fun CommentApiModel?.map(): CommentModel {
    return CommentModel(
        id = this?.id ?: 0,
        author = this?.author.map(),
        text = this?.text ?: EMPTY_STRING,
        createdAt = this?.createdAt ?: EMPTY_STRING,
        modifiedAt = this?.modifiedAt ?: EMPTY_STRING,
        post = this?.post ?: 0
    )
}