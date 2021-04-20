package kz.eztech.stylyts.domain.repository.collection_constructor

import io.reactivex.Single
import kz.eztech.stylyts.data.api.models.posts.TagsApiModel
import kz.eztech.stylyts.domain.models.PublicationModel
import okhttp3.MultipartBody

interface CollectionConstructorDomainRepository {

    fun createPost(
        token: String,
        description: String,
        imageList: List<MultipartBody.Part>,
        tags: TagsApiModel
    ): Single<PublicationModel>
}