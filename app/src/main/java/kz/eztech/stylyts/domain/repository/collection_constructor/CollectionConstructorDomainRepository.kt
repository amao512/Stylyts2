package kz.eztech.stylyts.domain.repository.collection_constructor

import io.reactivex.Single
import kz.eztech.stylyts.domain.models.PublicationModel
import okhttp3.MultipartBody

interface CollectionConstructorDomainRepository {

    fun createPost(
        token: String,
        bodyList: List<MultipartBody.Part>
    ): Single<PublicationModel>
}