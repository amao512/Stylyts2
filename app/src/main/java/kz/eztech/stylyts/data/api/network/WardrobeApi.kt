package kz.eztech.stylyts.data.api.network

import io.reactivex.Single
import kz.eztech.stylyts.data.api.RestConstants
import kz.eztech.stylyts.data.api.models.wardrobe.WardrobeApiModel
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface WardrobeApi {

    @Multipart
    @POST(RestConstants.CREATE_CLOTHES_BY_PHOTO)
    fun createClothesByPhoto(
        @Header("Authorization") token: String,
        @Part multipartList: ArrayList<MultipartBody.Part>
    ): Single<Response<WardrobeApiModel>>
}