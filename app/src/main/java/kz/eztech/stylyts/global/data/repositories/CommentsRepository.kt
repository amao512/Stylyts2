package kz.eztech.stylyts.global.data.repositories

import io.reactivex.Single
import kz.eztech.stylyts.global.data.models.comments.CommentCreateModel
import kz.eztech.stylyts.global.data.api.CommentsApi
import kz.eztech.stylyts.global.data.exception.NetworkException
import kz.eztech.stylyts.utils.mappers.comments.map
import kz.eztech.stylyts.utils.mappers.map
import kz.eztech.stylyts.global.domain.models.comments.CommentModel
import kz.eztech.stylyts.global.domain.models.common.ResultsModel
import kz.eztech.stylyts.global.domain.repositories.CommentsDomainRepository
import javax.inject.Inject

class CommentsRepository @Inject constructor(
    private val api: CommentsApi
) : CommentsDomainRepository {

    override fun getComments(map: Map<String, String>): Single<ResultsModel<CommentModel>> {
        return api.getComments( map).map {
            when (it.isSuccessful) {
                true -> it.body().map()
                false -> throw NetworkException(it)
            }
        }
    }

    override fun createComment(commentCreateModel: CommentCreateModel): Single<CommentModel> {
        return api.createComment(commentCreateModel).map {
            when (it.isSuccessful) {
                true -> it.body().map()
                false -> throw NetworkException(it)
            }
        }
    }
}