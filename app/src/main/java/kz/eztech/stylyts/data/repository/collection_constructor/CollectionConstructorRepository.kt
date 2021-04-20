package kz.eztech.stylyts.data.repository.collection_constructor

import io.reactivex.Single
import kz.eztech.stylyts.data.api.models.posts.TagsApiModel
import kz.eztech.stylyts.data.api.network.CollectionConstructorApi
import kz.eztech.stylyts.data.exception.NetworkException
import kz.eztech.stylyts.domain.models.PublicationModel
import kz.eztech.stylyts.domain.repository.collection_constructor.CollectionConstructorDomainRepository
import okhttp3.MultipartBody
import javax.inject.Inject

class CollectionConstructorRepository @Inject constructor(
    private val constructorApi: CollectionConstructorApi
) : CollectionConstructorDomainRepository {

    override fun createPost(
        token: String,
        description: String,
        imageList: List<MultipartBody.Part>,
        tags: TagsApiModel
    ): Single<PublicationModel> {
        return constructorApi.createPost(
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
}