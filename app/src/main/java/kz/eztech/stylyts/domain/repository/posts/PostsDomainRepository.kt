package kz.eztech.stylyts.domain.repository.posts

import io.reactivex.Single
import kz.eztech.stylyts.data.api.models.posts.TagsApiModel
import kz.eztech.stylyts.domain.models.PublicationModel
import kz.eztech.stylyts.domain.models.ResultsModel
import kz.eztech.stylyts.domain.models.posts.PostModel
import okhttp3.MultipartBody

interface PostsDomainRepository {

    fun createPost(
        token: String,
        description: String,
        imageList: List<MultipartBody.Part>,
        tags: TagsApiModel
    ): Single<PublicationModel>

    fun getPosts(token: String): Single<ResultsModel<PostModel>>
}