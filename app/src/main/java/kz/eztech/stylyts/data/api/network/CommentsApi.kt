package kz.eztech.stylyts.data.api.network

import io.reactivex.Single
import kz.eztech.stylyts.data.api.RestConstants
import kz.eztech.stylyts.data.api.models.ResultsApiModel
import kz.eztech.stylyts.data.api.models.comments.CommentApiModel
import kz.eztech.stylyts.data.api.models.comments.CommentCreateModel
import retrofit2.Response
import retrofit2.http.*

interface CommentsApi {

    @GET(RestConstants.GET_COMMENTS)
    fun getComments(
        @Header("Authorization") token: String,
        @QueryMap queryMap: Map<String, String>
    ): Single<Response<ResultsApiModel<CommentApiModel>>>

    @POST(RestConstants.CREATE_COMMENT)
    fun createComment(
        @Header("Authorization") token: String,
        @Body commentCreateModel: CommentCreateModel
    ): Single<Response<CommentApiModel>>
}