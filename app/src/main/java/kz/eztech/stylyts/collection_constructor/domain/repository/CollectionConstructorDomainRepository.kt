package kz.eztech.stylyts.collection_constructor.domain.repository

import io.reactivex.Single
import kz.eztech.stylyts.collection_constructor.domain.models.PublicationModel
import okhttp3.MultipartBody

interface CollectionConstructorDomainRepository {

    fun createPost(
        token: String,
        description: String,
        hidden: Boolean,
        tags: String,
        imageOne: MultipartBody.Part
    ): Single<PublicationModel>
}