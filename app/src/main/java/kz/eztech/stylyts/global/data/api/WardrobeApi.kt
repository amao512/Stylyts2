package kz.eztech.stylyts.global.data.api

import io.reactivex.Single
import kz.eztech.stylyts.global.data.constants.RestConstants
import kz.eztech.stylyts.global.data.models.clothes.ClothesApiModel
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface WardrobeApi {

    @Multipart
    @POST(RestConstants.CREATE_CLOTHES_BY_PHOTO)
    fun createClothesByPhoto(
        @Part multipartList: ArrayList<MultipartBody.Part>
    ): Single<Response<ClothesApiModel>>
}