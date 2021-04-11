package kz.eztech.stylyts.data.api

import io.reactivex.Single
import kz.eztech.stylyts.domain.models.ResultsModel
import kz.eztech.stylyts.domain.models.clothes.ClothesBrandModel
import kz.eztech.stylyts.domain.models.clothes.ClothesCategoryModel
import kz.eztech.stylyts.domain.models.clothes.ClothesModel
import kz.eztech.stylyts.domain.models.clothes.ClothesTypeModel
import retrofit2.Response
import retrofit2.http.*

/**
 * Created by Asylzhan Seytbek on 06.04.2021.
 */
interface ClothesApi {

    @GET(RestConstants.GET_CLOTHES_TYPES)
    fun getClothesTypes(
        @Header("Authorization") token: String
    ): Single<Response<ResultsModel<ClothesTypeModel>>>

    @GET(RestConstants.GET_CLOTHES_CATEGORIES)
    fun getClothesCategories(
        @Header("Authorization") token: String
    ): Single<Response<ResultsModel<ClothesCategoryModel>>>

    @GET(RestConstants.GET_CLOTHES_CATEGORIES)
    fun getClothesCategoriesByType(
        @Header("Authorization") token: String,
        @Query("clothes_type") clothesTypeId: String
    ): Single<Response<ResultsModel<ClothesCategoryModel>>>

    @GET(RestConstants.GET_CLOTHES_BY_ID)
    fun getClothesById(
        @Header("Authorization") token: String,
        @Path("id") clothesId: String
    ): Single<Response<ClothesModel>>

    @GET(RestConstants.GET_CLOTHES_BRANDS)
    fun getClothesBrands(
        @Header("Authorization") token: String
    ): Single<Response<ResultsModel<ClothesBrandModel>>>

    @GET(RestConstants.GET_CLOTHES_BRAND_BY_ID)
    fun getClothesBrandById(
        @Header("Authorization") token: String,
        @Path("brand_id") brandId: String
    ): Single<Response<ClothesBrandModel>>

    @GET(RestConstants.GET_CLOTHES)
    fun getClothes(
        @Header("Authorization") token: String,
        @QueryMap(encoded = true) queryMap: Map<String, String>
    ): Single<Response<ResultsModel<ClothesModel>>>
}