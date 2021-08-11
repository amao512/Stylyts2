package kz.eztech.stylyts.data.repository

import io.reactivex.Single
import kz.eztech.stylyts.data.api.models.comments.CommentCreateModel
import kz.eztech.stylyts.data.api.network.CommentsApi
import kz.eztech.stylyts.data.exception.NetworkException
import kz.eztech.stylyts.utils.mappers.comments.map
import kz.eztech.stylyts.utils.mappers.map
import kz.eztech.stylyts.domain.models.comments.CommentModel
import kz.eztech.stylyts.domain.models.common.ResultsModel
import kz.eztech.stylyts.domain.repository.CommentsDomainRepository
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