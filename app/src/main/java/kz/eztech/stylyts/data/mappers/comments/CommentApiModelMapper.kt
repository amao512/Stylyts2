package kz.eztech.stylyts.data.mappers.comments

import kz.eztech.stylyts.data.api.models.comments.CommentApiModel
import kz.eztech.stylyts.data.mappers.user.UserShortApiModelMapper
import kz.eztech.stylyts.domain.models.comments.CommentModel
import kz.eztech.stylyts.presentation.utils.EMPTY_STRING
import javax.inject.Inject

class CommentApiModelMapper @Inject constructor(
    private val userShortApiModelMapper: UserShortApiModelMapper
) {

    fun map(data: List<CommentApiModel>?): List<CommentModel> {
        data ?: return emptyList()

        return data.map {
            CommentModel(
                id = it.id ?: 0,
                author = userShortApiModelMapper.map(it.author),
                text = it.text ?: EMPTY_STRING,
                createdAt = it.createdAt ?: EMPTY_STRING,
                modifiedAt = it.modifiedAt ?: EMPTY_STRING,
                post = it.post ?: 0
            )
        }
    }

    fun map(data: CommentApiModel?): CommentModel {
        return CommentModel(
            id = data?.id ?: 0,
            author = userShortApiModelMapper.map(data?.author),
            text = data?.text ?: EMPTY_STRING,
            createdAt = data?.createdAt ?: EMPTY_STRING,
            modifiedAt = data?.modifiedAt ?: EMPTY_STRING,
            post = data?.post ?: 0
        )
    }
}