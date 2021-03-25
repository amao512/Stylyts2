package kz.eztech.stylyts.data.api

import io.reactivex.Single
import kz.eztech.stylyts.domain.models.ClothesMainModel
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

/**
 * Created by Asylzhan Seytbek on 25.03.2021.
 */
interface CollectionApi {

    @GET(RestConstants.GET_ITEM_DETAIL)
    fun getItemDetail(
        @Header("Authorization") token: String,
        @Path("id") id: Int
    ): Single<Response<ClothesMainModel>>

    @FormUrlEncoded
    @POST(RestConstants.GET_ITEM_BY_BARCODE)
    fun getItemByBarcode(
        @Header("Authorization") token: String,
        @Field("barcode") value: String
    ): Single<Response<ClothesMainModel>>

    @Multipart
    @POST(RestConstants.SAVE_ITEM_BY_PHOTO)
    fun saveItemByPhoto(
        @Header("Authorization") token: String,
        @Part files: ArrayList<MultipartBody.Part>
    ): Single<Response<ClothesMainModel>>
}