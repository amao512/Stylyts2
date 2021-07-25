package kz.eztech.stylyts.domain.repository

import io.reactivex.Single
import kz.eztech.stylyts.data.api.models.comments.CommentCreateModel
import kz.eztech.stylyts.domain.models.common.ResultsModel
import kz.eztech.stylyts.domain.models.comments.CommentModel

interface CommentsDomainRepository {

    fun getComments(map: Map<String, String>): Single<ResultsModel<CommentModel>>

    fun createComment(commentCreateModel: CommentCreateModel): Single<CommentModel>
}