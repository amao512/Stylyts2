package kz.eztech.stylyts.data.api.network

import io.reactivex.Single
import kz.eztech.stylyts.data.api.RestConstants
import kz.eztech.stylyts.data.api.models.ResultsApiModel
import kz.eztech.stylyts.data.api.models.outfits.OutfitApiModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header

interface OutfitsApi {

    @GET(RestConstants.GET_OUTFITS)
    fun getOutfits(
        @Header("Authorization") token: String
    ): Single<Response<ResultsApiModel<OutfitApiModel>>>
}