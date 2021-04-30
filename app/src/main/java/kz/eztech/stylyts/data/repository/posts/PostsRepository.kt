package kz.eztech.stylyts.data.repository.posts

import android.util.Log
import io.reactivex.Single
import kz.eztech.stylyts.data.api.models.posts.TagsApiModel
import kz.eztech.stylyts.data.api.network.PostsApi
import kz.eztech.stylyts.data.exception.NetworkException
import kz.eztech.stylyts.data.mappers.ActionApiModelMapper
import kz.eztech.stylyts.data.mappers.ResultsApiModelMapper
import kz.eztech.stylyts.data.mappers.posts.PostApiModelMapper
import kz.eztech.stylyts.domain.models.ActionModel
import kz.eztech.stylyts.domain.models.ResultsModel
import kz.eztech.stylyts.domain.models.posts.PostModel
import kz.eztech.stylyts.domain.repository.posts.PostsDomainRepository
import okhttp3.MultipartBody
import javax.inject.Inject

class PostsRepository @Inject constructor(
    private val api: PostsApi,
    private val resultsApiModelMapper: ResultsApiModelMapper,
    private val postApiModelMapper: PostApiModelMapper,
    private val actionApiModelMapper: ActionApiModelMapper
) : PostsDomainRepository {

    override fun createPost(
        token: String,
        multipartList: List<MultipartBody.Part>,
        tags: TagsApiModel
    ): Single<PostModel> {
        return api.createPost(
            token = token,
            multipartList = multipartList,
            tagsBody = tags
        ).map {
            when (it.isSuccessful) {
                true -> postApiModelMapper.map(data = it.body())
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
                true -> resultsApiModelMapper.mapPostResults(data = it.body())
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
                true -> resultsApiModelMapper.mapPostResults(data = it.body())
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
        multipartList: List<MultipartBody.Part>,
        tags: TagsApiModel
    ): Single<PostModel> {
        return api.updatePost(
            token = token,
            postId = postId,
            multipartList = multipartList,
            tagsBody = tags
        ).map {
            Log.d("TAG4", it.toString())
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