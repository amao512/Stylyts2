package kz.eztech.stylyts.collection_constructor.data.repository

import io.reactivex.Single
import kz.eztech.stylyts.common.data.exception.NetworkException
import kz.eztech.stylyts.collection_constructor.data.api.CollectionConstructorApi
import kz.eztech.stylyts.collection_constructor.domain.models.PublicationModel
import kz.eztech.stylyts.collection_constructor.domain.repository.CollectionConstructorDomainRepository
import okhttp3.MultipartBody
import javax.inject.Inject

class CollectionConstructorRepository @Inject constructor(
    private val constructorApi: CollectionConstructorApi
) : CollectionConstructorDomainRepository {

    override fun createPost(
        token: String,
        description: String,
        hidden: Boolean,
        tags: String,
        imageOne: MultipartBody.Part
    ): Single<PublicationModel> {
        return constructorApi.createPost(
            token = token,
            description = description,
            hidden = hidden,
            tags = tags,
            imageOne = imageOne
        ).map {
            when (it.isSuccessful) {
                true -> it.body()
                else -> throw NetworkException(it)
            }
        }
    }
}