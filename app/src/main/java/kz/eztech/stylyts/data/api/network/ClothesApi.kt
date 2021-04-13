package kz.eztech.stylyts.data.api.network

import io.reactivex.Single
import kz.eztech.stylyts.data.api.RestConstants
import kz.eztech.stylyts.data.api.models.ResultsApiModel
import kz.eztech.stylyts.data.api.models.clothes.ClothesBrandApiModel
import kz.eztech.stylyts.data.api.models.clothes.ClothesCategoryApiModel
import kz.eztech.stylyts.data.api.models.clothes.ClothesApiModel
import kz.eztech.stylyts.data.api.models.clothes.ClothesTypeApiModel
import retrofit2.Response
import retrofit2.http.*

/**
 * Created by Asylzhan Seytbek on 06.04.2021.
 */
interface ClothesApi {

    @GET(RestConstants.GET_CLOTHES_TYPES)
    fun getClothesTypes(
        @Header("Authorization") token: String
    ): Single<Response<ResultsApiModel<ClothesTypeApiModel>>>

    @GET(RestConstants.GET_CLOTHES_CATEGORIES)
    fun getClothesCategories(
        @Header("Authorization") token: String
    ): Single<Response<ResultsApiModel<ClothesCategoryApiModel>>>

    @GET(RestConstants.GET_CLOTHES_CATEGORIES)
    fun getClothesCategoriesByType(
        @Header("Authorization") token: String,
        @Query("clothes_type") clothesTypeId: String
    ): Single<Response<ResultsApiModel<ClothesCategoryApiModel>>>

    @GET(RestConstants.GET_CLOTHES_BY_ID)
    fun getClothesById(
        @Header("Authorization") token: String,
        @Path("id") clothesId: String
    ): Single<Response<ClothesApiModel>>

    @GET(RestConstants.GET_CLOTHES_BRANDS)
    fun getClothesBrands(
        @Header("Authorization") token: String
    ): Single<Response<ResultsApiModel<ClothesBrandApiModel>>>

    @GET(RestConstants.GET_CLOTHES_BRAND_BY_ID)
    fun getClothesBrandById(
        @Header("Authorization") token: String,
        @Path("brand_id") brandId: String
    ): Single<Response<ClothesBrandApiModel>>

    @GET(RestConstants.GET_CLOTHES)
    fun getClothes(
        @Header("Authorization") token: String,
        @QueryMap(encoded = true) stringQueryMap: Map<String, String>,
        @QueryMap(encoded = true) booleanQueryMap: Map<String, Boolean>
    ): Single<Response<ResultsApiModel<ClothesApiModel>>>

    @POST(RestConstants.SAVE_CLOTHES_TO_WARDROBE)
    fun saveClothesToWardrobe(
        @Header("Authorization") token: String,
        @Path("clothes_id") clothesId: String
    ): Single<Response<Any>>
}