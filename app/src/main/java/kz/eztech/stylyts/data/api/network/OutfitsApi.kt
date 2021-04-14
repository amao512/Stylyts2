package kz.eztech.stylyts.data.api.network

import io.reactivex.Single
import kz.eztech.stylyts.data.api.RestConstants
import kz.eztech.stylyts.data.api.models.ResultsApiModel
import kz.eztech.stylyts.data.api.models.outfits.OutfitApiModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.QueryMap

interface OutfitsApi {

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
}