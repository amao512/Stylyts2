package kz.eztech.stylyts.data.api.network

import io.reactivex.Single
import kz.eztech.stylyts.data.api.RestConstants
import kz.eztech.stylyts.data.api.models.ResultsApiModel
import kz.eztech.stylyts.data.api.models.posts.PostApiModel
import kz.eztech.stylyts.data.api.models.posts.TagsApiModel
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

interface PostsApi {

    @Multipart
    @POST(RestConstants.CREATE_POST)
    fun createPost(
        @Header("Authorization") token: String,
        @Part multipartList: List<MultipartBody.Part>,
        @Part("tags") tagsBody: TagsApiModel
    ) : Single<Response<PostApiModel>>

    @GET(RestConstants.GET_POSTS)
    fun getPosts(
        @Header("Authorization") token: String,
        @QueryMap queryMap: Map<String, String>
    ): Single<Response<ResultsApiModel<PostApiModel>>>

    @GET(RestConstants.GET_POST_BY_ID)
    fun getPostById(
        @Header("Authorization") token: String,
        @Path("post_id") postId: String
    ): Single<Response<PostApiModel>>

    @GET(RestConstants.GET_HOMEPAGE_POSTS)
    fun getHomePagePosts(
        @Header("Authorization") token: String,
        @QueryMap queryMap: Map<String, String>
    ): Single<Response<ResultsApiModel<PostApiModel>>>

    @DELETE(RestConstants.DELETE_POST_BY_ID)
    fun deletePost(
        @Header("Authorization") token: String,
        @Path("post_id") postId: String
    ): Single<Response<Any>>

    @Multipart
    @PATCH(RestConstants.UPDATE_POST)
    fun updatePost(
        @Header("Authorization") token: String,
        @Path("post_id") postId: String,
        @Part multipartList: List<MultipartBody.Part>,
        @Part("tags") tagsBody: TagsApiModel
    ) : Single<Response<PostApiModel>>
}