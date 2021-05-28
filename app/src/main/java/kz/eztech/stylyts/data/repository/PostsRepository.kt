package kz.eztech.stylyts.data.repository

import io.reactivex.Single
import kz.eztech.stylyts.data.api.models.posts.TagsApiModel
import kz.eztech.stylyts.data.api.network.PostsApi
import kz.eztech.stylyts.data.exception.NetworkException
import kz.eztech.stylyts.data.mappers.ActionApiModelMapper
import kz.eztech.stylyts.data.mappers.ResultsApiModelMapper
import kz.eztech.stylyts.data.mappers.posts.PostApiModelMapper
import kz.eztech.stylyts.data.mappers.posts.PostCreateApiModelMapper
import kz.eztech.stylyts.domain.models.common.ActionModel
import kz.eztech.stylyts.domain.models.common.ResultsModel
import kz.eztech.stylyts.domain.models.posts.PostCreateModel
import kz.eztech.stylyts.domain.models.posts.PostModel
import kz.eztech.stylyts.domain.repository.PostsDomainRepository
import okhttp3.MultipartBody
import javax.inject.Inject

class PostsRepository @Inject constructor(
    private val api: PostsApi,
    private val resultsApiModelMapper: ResultsApiModelMapper,
    private val postApiModelMapper: PostApiModelMapper,
    private val actionApiModelMapper: ActionApiModelMapper,
    private val postCreateApiModelMapper: PostCreateApiModelMapper
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
                true -> postCreateApiModelMapper.map(data = it.body())
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
                true -> resultsApiModelMapper.map(data = it.body())
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
                true -> postApiModelMapper.map(data = it.body())
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
                true -> resultsApiModelMapper.map(data = it.body())
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
    ): Single<PostModel> {
        return api.updatePost(
            token = token,
            postId = postId,
            tagsBody = tags,
            multipartList = multipartList
        ).map {
            when (it.isSuccessful) {
                true -> postApiModelMapper.map(data = it.body())
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
                true -> actionApiModelMapper.map(data = it.body())
                else -> throw NetworkException(it)
            }
        }
    }
}