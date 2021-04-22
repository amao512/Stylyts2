package kz.eztech.stylyts.data.api.network

import io.reactivex.Single
import kz.eztech.stylyts.data.api.RestConstants
import kz.eztech.stylyts.data.api.models.ResultsApiModel
import kz.eztech.stylyts.data.api.models.outfits.OutfitApiModel
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

interface OutfitsApi {

    @Multipart
    @POST(RestConstants.CREATE_OUTFIT)
    fun saveOutfit(
        @Header("Authorization") token: String,
        @Part files: ArrayList<MultipartBody.Part>
    ): Single<Response<OutfitApiModel>>

    @GET(RestConstants.GET_OUTFITS)
    fun getOutfits(
        @Header("Authorization") token: String,
        @QueryMap booleanQueryMap: Map<String, Boolean>
    ): Single<Response<ResultsApiModel<OutfitApiModel>>>

    @GET(RestConstants.GET_OUTFIT_BY_ID)
    fun getOutfitById(
        @Header("Authorization") token: String,
        @Path("outfit_id") outfitId: String
    ): Single<Response<OutfitApiModel>>

    @DELETE(RestConstants.DELETE_OUTFIT_BY_ID)
    fun deleteOutfit(
        @Header("Authorization") token: String,
        @Path("outfit_id") outfitId: String
    ): Single<Response<Any>>
}