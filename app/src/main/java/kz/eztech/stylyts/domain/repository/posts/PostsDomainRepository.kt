package kz.eztech.stylyts.domain.repository.posts

import io.reactivex.Single
import kz.eztech.stylyts.data.api.models.posts.TagsApiModel
import kz.eztech.stylyts.domain.models.ActionModel
import kz.eztech.stylyts.domain.models.ResultsModel
import kz.eztech.stylyts.domain.models.posts.PostModel
import okhttp3.MultipartBody

interface PostsDomainRepository {

    fun createPost(
        token: String,
        multipartList: List<MultipartBody.Part>,
        tags: TagsApiModel
    ): Single<PostModel>

    fun getPosts(
        token: String,
        queryMap: Map<String, String>
    ): Single<ResultsModel<PostModel>>

    fun getPostById(
        token: String,
        postId: String
    ): Single<PostModel>

    fun getHomepagePosts(
        token: String,
        queryMap: Map<String, String>
    ): Single<ResultsModel<PostModel>>

    fun deletePost(
        token: String,
        postId: String
    ): Single<Any>

    fun updatePost(
        token: String,
        postId: String,
        multipartList: List<MultipartBody.Part>,
        tags: TagsApiModel
    ): Single<PostModel>

    fun likePost(
        token: String,
        postId: String,
    ): Single<ActionModel>
}