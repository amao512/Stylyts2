package kz.eztech.stylyts.domain.repository

import io.reactivex.Single
import kz.eztech.stylyts.data.api.models.posts.TagsApiModel
import kz.eztech.stylyts.domain.models.common.ActionModel
import kz.eztech.stylyts.domain.models.common.ResultsModel
import kz.eztech.stylyts.domain.models.posts.PostCreateModel
import kz.eztech.stylyts.domain.models.posts.PostModel
import okhttp3.MultipartBody

interface PostsDomainRepository {

    fun createPost(
        token: String,
        multipartList: List<MultipartBody.Part>,
        tags: TagsApiModel
    ): Single<PostCreateModel>

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
        description: MultipartBody.Part,
        tags: TagsApiModel,
        hidden: MultipartBody.Part
    ): Single<PostModel>

    fun likePost(
        token: String,
        postId: String,
    ): Single<ActionModel>
}