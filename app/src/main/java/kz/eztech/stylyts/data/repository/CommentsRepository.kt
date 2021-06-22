package kz.eztech.stylyts.data.repository

import io.reactivex.Single
import kz.eztech.stylyts.data.api.models.comments.CommentCreateModel
import kz.eztech.stylyts.data.api.network.CommentsApi
import kz.eztech.stylyts.data.exception.NetworkException
import kz.eztech.stylyts.presentation.utils.extensions.mappers.comments.map
import kz.eztech.stylyts.presentation.utils.extensions.mappers.map
import kz.eztech.stylyts.domain.models.comments.CommentModel
import kz.eztech.stylyts.domain.models.common.ResultsModel
import kz.eztech.stylyts.domain.repository.CommentsDomainRepository
import javax.inject.Inject

class CommentsRepository @Inject constructor(
    private val api: CommentsApi
) : CommentsDomainRepository {

    override fun getComments(
        token: String,
        postId: String
    ): Single<ResultsModel<CommentModel>> {
        return api.getComments(token, postId).map {
            when (it.isSuccessful) {
                true -> it.body().map()
                false -> throw NetworkException(it)
            }
        }
    }

    override fun createComment(
        token: String,
        commentCreateModel: CommentCreateModel
    ): Single<CommentModel> {
        return api.createComment(token, commentCreateModel).map {
            when (it.isSuccessful) {
                true -> it.body().map()
                false -> throw NetworkException(it)
            }
        }
    }
}