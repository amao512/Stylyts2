package kz.eztech.stylyts.data.api

import io.reactivex.Single
import kz.eztech.stylyts.domain.models.ResultsModel
import kz.eztech.stylyts.domain.models.clothes.ClothesCategoryModel
import kz.eztech.stylyts.domain.models.clothes.ClothesModel
import kz.eztech.stylyts.domain.models.clothes.ClothesTypeModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

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

    @GET(RestConstants.GET_CLOTHES)
    fun getClothesByType(
        @Header("Authorization") token: String,
        @Query("clothes_type") clothesTypeId: String,
        @Query("gender") gender: String
    ): Single<Response<ResultsModel<ClothesModel>>>

    @GET(RestConstants.GET_CLOTHES)
    fun getClothesByCategory(
        @Header("Authorization") token: String,
        @Query("gender") gender: String,
        @Query("clothes_category") clothesCategoryId: String
    ): Single<Response<ResultsModel<ClothesModel>>>

    @GET(RestConstants.GET_CLOTHES_BY_ID)
    fun getClothesById(
        @Header("Authorization") token: String,
        @Path("id") clothesId: String
    ): Single<Response<ClothesModel>>
}