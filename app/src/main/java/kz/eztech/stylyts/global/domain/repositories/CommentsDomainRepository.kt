package kz.eztech.stylyts.global.domain.repositories

import io.reactivex.Single
import kz.eztech.stylyts.global.data.models.comments.CommentCreateModel
import kz.eztech.stylyts.global.domain.models.common.ResultsModel
import kz.eztech.stylyts.global.domain.models.comments.CommentModel

interface CommentsDomainRepository {

    fun getComments(map: Map<String, String>): Single<ResultsModel<CommentModel>>

    fun createComment(commentCreateModel: CommentCreateModel): Single<CommentModel>
}