package kz.eztech.stylyts.global.data.api

import io.reactivex.Single
import kz.eztech.stylyts.global.data.constants.RestConstants
import kz.eztech.stylyts.global.data.models.ResultsApiModel
import kz.eztech.stylyts.global.domain.models.outfits.OutfitApiModel
import kz.eztech.stylyts.global.domain.models.outfits.OutfitCreateApiModel
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

interface OutfitsApi {

    @Multipart
    @POST(RestConstants.CREATE_OUTFIT)
    fun saveOutfit(
        @Part files: ArrayList<MultipartBody.Part>
    ): Single<Response<OutfitCreateApiModel>>

    @GET(RestConstants.GET_OUTFITS)
    fun getOutfits(
        @QueryMap booleanQueryMap: Map<String, Boolean>,
        @QueryMap stringQueryMap: Map<String, String>
    ): Single<Response<ResultsApiModel<OutfitApiModel>>>

    @GET(RestConstants.GET_OUTFIT_BY_ID)
    fun getOutfitById(
        @Path("outfit_id") outfitId: String
    ): Single<Response<OutfitApiModel>>

    @DELETE(RestConstants.DELETE_OUTFIT_BY_ID)
    fun deleteOutfit(
        @Path("outfit_id") outfitId: String
    ): Single<Response<Any>>

    @Multipart
    @PATCH(RestConstants.UPDATE_OUTFIT)
    fun updateOutfit(
        @Path("outfit_id") outfitId: String,
        @Part files: ArrayList<MultipartBody.Part>
    ): Single<Response<OutfitCreateApiModel>>
}