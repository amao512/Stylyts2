package kz.eztech.stylyts.global.data.api

import io.reactivex.Single
import kz.eztech.stylyts.global.data.constants.RestConstants
import kz.eztech.stylyts.global.data.models.ResultsApiModel
import kz.eztech.stylyts.global.data.models.comments.CommentApiModel
import kz.eztech.stylyts.global.data.models.comments.CommentCreateModel
import retrofit2.Response
import retrofit2.http.*

interface CommentsApi {

    @GET(RestConstants.GET_COMMENTS)
    fun getComments(
        @QueryMap queryMap: Map<String, String>
    ): Single<Response<ResultsApiModel<CommentApiModel>>>

    @POST(RestConstants.CREATE_COMMENT)
    fun createComment(
        @Body commentCreateModel: CommentCreateModel
    ): Single<Response<CommentApiModel>>
}