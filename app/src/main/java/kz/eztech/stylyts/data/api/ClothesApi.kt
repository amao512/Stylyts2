package kz.eztech.stylyts.data.api

import io.reactivex.Single
import kz.eztech.stylyts.domain.models.ResultsModel
import kz.eztech.stylyts.domain.models.clothes.ClothesTypeModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header

/**
 * Created by Asylzhan Seytbek on 06.04.2021.
 */
interface ClothesApi {

    @GET(RestConstants.GET_CLOTHES_TYPES)
    fun getClothesTypes(
        @Header("Authorization") token: String
    ): Single<Response<ResultsModel<ClothesTypeModel>>>
}