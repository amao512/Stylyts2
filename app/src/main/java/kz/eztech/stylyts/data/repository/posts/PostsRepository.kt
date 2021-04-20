package kz.eztech.stylyts.data.repository.posts

import io.reactivex.Single
import kz.eztech.stylyts.data.api.models.posts.TagsApiModel
import kz.eztech.stylyts.data.api.network.PostsApi
import kz.eztech.stylyts.data.exception.NetworkException
import kz.eztech.stylyts.data.mappers.ResultsApiModelMapper
import kz.eztech.stylyts.domain.models.PublicationModel
import kz.eztech.stylyts.domain.models.ResultsModel
import kz.eztech.stylyts.domain.models.posts.PostModel
import kz.eztech.stylyts.domain.repository.posts.PostsDomainRepository
import okhttp3.MultipartBody
import javax.inject.Inject

class PostsRepository @Inject constructor(
    private val api: PostsApi,
    private val resultsApiModelMapper: ResultsApiModelMapper
) : PostsDomainRepository {

    override fun createPost(
        token: String,
        description: String,
        imageList: List<MultipartBody.Part>,
        tags: TagsApiModel
    ): Single<PublicationModel> {
        return api.createPost(
            token = token,
            description = description,
            imageList = imageList,
            tagsBody = tags
        ).map {
            when (it.isSuccessful) {
                true -> it.body()
                else -> throw NetworkException(it)
            }
        }
    }

    override fun getPosts(token: String): Single<ResultsModel<PostModel>> {
        return api.getPosts(token).map {
            when (it.isSuccessful) {
                true -> resultsApiModelMapper.mapPostResults(data = it.body())
                false -> throw NetworkException(it)
            }
        }
    }
}