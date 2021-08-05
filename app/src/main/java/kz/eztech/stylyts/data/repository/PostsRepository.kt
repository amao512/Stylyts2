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
import kz.eztech.stylyts.presentation.utils.mappers.map
import kz.eztech.stylyts.presentation.utils.mappers.posts.map
import okhttp3.MultipartBody
import javax.inject.Inject

class PostsRepository @Inject constructor(
    private val api: PostsApi
) : PostsDomainRepository {

    override fun createPost(
        multipartList: List<MultipartBody.Part>,
        tags: TagsApiModel
    ): Single<PostCreateModel> {
        return api.createPost(
            multipartList = multipartList,
            tagsBody = tags
        ).map {
            when (it.isSuccessful) {
                true -> it.body().map()
                else -> throw NetworkException(it)
            }
        }
    }

    override fun getPosts(queryMap: Map<String, String>): Single<ResultsModel<PostModel>> {
        return api.getPosts(queryMap).map {
            when (it.isSuccessful) {
                true -> it.body().map()
                false -> throw NetworkException(it)
            }
        }
    }

    override fun getPostById(postId: String): Single<PostModel> {
        return api.getPostById(postId).map {
            when (it.isSuccessful) {
                true -> it.body().map()
                false -> throw NetworkException(it)
            }
        }
    }

    override fun getHomepagePosts(queryMap: Map<String, String>): Single<ResultsModel<PostModel>> {
        return api.getHomePagePosts(queryMap).map {
            when (it.isSuccessful) {
                true -> it.body().map()
                false -> throw NetworkException(it)
            }
        }
    }

    override fun deletePost(postId: String): Single<Any> {
        return api.deletePost(postId).map {
            when (it.isSuccessful) {
                true -> it.body()
                false -> throw NetworkException(it)
            }
        }
    }

    override fun updatePost(
        postId: String,
        tags: TagsApiModel,
        multipartList: List<MultipartBody.Part>
    ): Single<PostCreateModel> {
        return api.updatePost(
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

    override fun likePost(postId: String): Single<ActionModel> {
        return api.likePost(postId).map {
            when (it.isSuccessful) {
                true -> it.body().map()
                else -> throw NetworkException(it)
            }
        }
    }
}