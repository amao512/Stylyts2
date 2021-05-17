package kz.eztech.stylyts.domain.repository

import io.reactivex.Single
import kz.eztech.stylyts.data.api.models.comments.CommentCreateModel
import kz.eztech.stylyts.domain.models.common.ResultsModel
import kz.eztech.stylyts.domain.models.comments.CommentModel

interface CommentsDomainRepository {

    fun getComments(
        token: String,
        postId: String
    ): Single<ResultsModel<CommentModel>>

    fun createComment(
        token: String,
        commentCreateModel: CommentCreateModel
    ): Single<CommentModel>
}