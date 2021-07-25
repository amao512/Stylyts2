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
        multipartList: List<MultipartBody.Part>,
        tags: TagsApiModel
    ): Single<PostCreateModel>

    fun getPosts(
        queryMap: Map<String, String>
    ): Single<ResultsModel<PostModel>>

    fun getPostById(postId: String): Single<PostModel>

    fun getHomepagePosts(queryMap: Map<String, String>): Single<ResultsModel<PostModel>>

    fun deletePost(postId: String): Single<Any>

    fun updatePost(
        postId: String,
        tags: TagsApiModel,
        multipartList: List<MultipartBody.Part>
    ): Single<PostCreateModel>

    fun likePost(postId: String, ): Single<ActionModel>
}