package kz.eztech.stylyts.data.api.network

import io.reactivex.Single
import kz.eztech.stylyts.data.api.RestConstants
import kz.eztech.stylyts.data.api.models.ActionApiModel
import kz.eztech.stylyts.data.api.models.ResultsApiModel
import kz.eztech.stylyts.data.api.models.posts.PostApiModel
import kz.eztech.stylyts.data.api.models.posts.PostCreateApiModel
import kz.eztech.stylyts.data.api.models.posts.TagsApiModel
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

interface PostsApi {

    @Multipart
    @POST(RestConstants.CREATE_POST)
    fun createPost(
        @Part multipartList: List<MultipartBody.Part>,
        @Part("tags") tagsBody: TagsApiModel
    ): Single<Response<PostCreateApiModel>>

    @GET(RestConstants.GET_POSTS)
    fun getPosts(
        @QueryMap queryMap: Map<String, String>
    ): Single<Response<ResultsApiModel<PostApiModel>>>

    @GET(RestConstants.GET_POST_BY_ID)
    fun getPostById(
        @Path("post_id") postId: String
    ): Single<Response<PostApiModel>>

    @GET(RestConstants.GET_HOMEPAGE_POSTS)
    fun getHomePagePosts(
        @QueryMap queryMap: Map<String, String>
    ): Single<Response<ResultsApiModel<PostApiModel>>>

    @DELETE(RestConstants.DELETE_POST_BY_ID)
    fun deletePost(
        @Path("post_id") postId: String
    ): Single<Response<Any>>

    @Multipart
    @PATCH(RestConstants.UPDATE_POST)
    fun updatePost(
        @Path("post_id") postId: String,
        @Part("tags") tagsBody: TagsApiModel,
        @Part multipartList: List<MultipartBody.Part>,
    ): Single<Response<PostCreateApiModel>>

    @POST(RestConstants.LIKE_POST)
    fun likePost(
        @Path("post_id") postId: String,
    ): Single<Response<ActionApiModel>>
}