package kz.eztech.stylyts.data.api.network

import io.reactivex.Single
import kz.eztech.stylyts.data.api.RestConstants
import kz.eztech.stylyts.data.api.models.ResultsApiModel
import kz.eztech.stylyts.data.api.models.posts.PostApiModel
import kz.eztech.stylyts.data.api.models.posts.TagsApiModel
import kz.eztech.stylyts.domain.models.PublicationModel
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

interface PostsApi {

    @Multipart
    @POST(RestConstants.CREATE_POST)
    fun createPost(
        @Header("Authorization") token: String,
        @Part("description") description: String,
        @Part imageList: List<MultipartBody.Part>,
        @Part("tags") tagsBody: TagsApiModel
    ) : Single<Response<PublicationModel>>

    @GET(RestConstants.GET_POSTS)
    fun getPosts(
        @Header("Authorization") token: String
    ): Single<Response<ResultsApiModel<PostApiModel>>>
}