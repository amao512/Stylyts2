package kz.eztech.stylyts.data.repository

import io.reactivex.Single
import kz.eztech.stylyts.data.api.models.posts.TagsApiModel
import kz.eztech.stylyts.data.api.network.PostsApi
import kz.eztech.stylyts.data.exception.NetworkException
import kz.eztech.stylyts.domain.models.common.ActionModel
import kz.eztech.stylyts.domain.models.common.ResultsModel
import kz.eztech.stylyts.domain.models.posts.PostCreateModel
import kz.eztech.stylyts.domain.models.posts.PostModel
import kz.eztech.stylyts.domain.repository.PostsDomainRepository
import kz.eztech.stylyts.presentation.utils.extensions.mappers.map
import kz.eztech.stylyts.presentation.utils.extensions.mappers.posts.map
import okhttp3.MultipartBody
import javax.inject.Inject

class PostsRepository @Inject constructor(
    private val api: PostsApi
) : PostsDomainRepository {

    override fun createPost(
        token: String,
        multipartList: List<MultipartBody.Part>,
        tags: TagsApiModel
    ): Single<PostCreateModel> {
        return api.createPost(
            token = token,
            multipartList = multipartList,
            tagsBody = tags
        ).map {
            when (it.isSuccessful) {
                true -> it.body().map()
                else -> throw NetworkException(it)
            }
        }
    }

    override fun getPosts(
        token: String,
        queryMap: Map<String, String>
    ): Single<ResultsModel<PostModel>> {
        return api.getPosts(token, queryMap).map {
            when (it.isSuccessful) {
                true -> it.body().map()
                false -> throw NetworkException(it)
            }
        }
    }

    override fun getPostById(
        token: String,
        postId: String
    ): Single<PostModel> {
        return api.getPostById(token, postId).map {
            when (it.isSuccessful) {
                true -> it.body().map()
                false -> throw NetworkException(it)
            }
        }
    }

    override fun getHomepagePosts(
        token: String,
        queryMap: Map<String, String>
    ): Single<ResultsModel<PostModel>> {
        return api.getHomePagePosts(token, queryMap).map {
            when (it.isSuccessful) {
                true -> it.body().map()
                false -> throw NetworkException(it)
            }
        }
    }

    override fun deletePost(
        token: String,
        postId: String
    ): Single<Any> {
        return api.deletePost(token, postId).map {
            when (it.isSuccessful) {
                true -> it.body()
                false -> throw NetworkException(it)
            }
        }
    }

    override fun updatePost(
        token: String,
        postId: String,
        tags: TagsApiModel,
        multipartList: List<MultipartBody.Part>
    ): Single<PostCreateModel> {
        return api.updatePost(
            token = token,
            postId = postId,
            tagsBody = tags,
            multipartList = multipartList
        ).map {
            when (it.isSuccessful) {
                true -> it.body().map()
                else -> throw NetworkException(it)
            }
        }
    }

    override fun likePost(
        token: String,
        postId: String
    ): Single<ActionModel> {
        return api.likePost(
            token = token,
            postId = postId
        ).map {
            when (it.isSuccessful) {
                true -> it.body().map()
                else -> throw NetworkException(it)
            }
        }
    }
}