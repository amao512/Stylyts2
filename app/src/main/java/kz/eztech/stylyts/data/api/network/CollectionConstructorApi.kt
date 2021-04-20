package kz.eztech.stylyts.data.api.network

import io.reactivex.Single
import kz.eztech.stylyts.data.api.RestConstants
import kz.eztech.stylyts.data.api.models.posts.TagsApiModel
import kz.eztech.stylyts.domain.models.PublicationModel
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

interface CollectionConstructorApi {

    @Multipart
    @POST(RestConstants.CREATE_POST)
    fun createPost(
        @Header("Authorization") token: String,
        @Part("description") description: String,
        @Part imageList: List<MultipartBody.Part>,
        @Part("tags") tagsBody: TagsApiModel
    ) : Single<Response<PublicationModel>>
}