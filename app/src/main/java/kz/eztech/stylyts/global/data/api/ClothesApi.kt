package kz.eztech.stylyts.global.data.api

import io.reactivex.Single
import kz.eztech.stylyts.global.data.constants.RestConstants
import kz.eztech.stylyts.global.data.models.ResultsApiModel
import kz.eztech.stylyts.global.data.models.clothes.*
import retrofit2.Response
import retrofit2.http.*

/**
 * Created by Asylzhan Seytbek on 06.04.2021.
 */
interface ClothesApi {

    @GET(RestConstants.GET_CLOTHES_TYPES)
    fun getClothesTypes(
        @QueryMap map: Map<String, String>
    ): Single<Response<ResultsApiModel<ClothesTypeApiModel>>>

    @GET(RestConstants.GET_CLOTHES_STYLES)
    fun getClothesStyles(
        @QueryMap map: Map<String, String>
    ): Single<Response<ResultsApiModel<ClothesStyleApiModel>>>

    @GET(RestConstants.GET_CLOTHES_CATEGORIES)
    fun getClothesCategories(
        @QueryMap map: Map<String, String>
    ): Single<Response<ResultsApiModel<ClothesCategoryApiModel>>>

    @GET(RestConstants.GET_CLOTHES_BY_ID)
    fun getClothesById(
        @Path("id") clothesId: String
    ): Single<Response<ClothesApiModel>>

    @GET(RestConstants.GET_CLOTHES_BRANDS)
    fun getClothesBrands(
        @QueryMap map: Map<String, String>
    ): Single<Response<ResultsApiModel<ClothesBrandApiModel>>>

    @GET(RestConstants.GET_CLOTHES_BRAND_BY_ID)
    fun getClothesBrandById(
        @Path("brand_id") brandId: String
    ): Single<Response<ClothesBrandApiModel>>

    @GET(RestConstants.GET_CLOTHES_COLORS)
    fun getClothesColors(
        @QueryMap map: Map<String, String>
    ): Single<Response<ResultsApiModel<ClothesColorApiModel>>>

    @GET(RestConstants.GET_CLOTHES)
    fun getClothes(
        @QueryMap(encoded = true) stringQueryMap: Map<String, String>,
        @QueryMap(encoded = true) booleanQueryMap: Map<String, Boolean>
    ): Single<Response<ResultsApiModel<ClothesApiModel>>>

    @POST(RestConstants.SAVE_CLOTHES_TO_WARDROBE)
    fun saveClothesToWardrobe(
        @Path("clothes_id") clothesId: String
    ): Single<Response<Any>>

    @GET(RestConstants.GET_CLOTHES_BY_BARCODE)
    fun getClothesByBarcode(
        @Path("barcode") barcode: String
    ): Single<Response<ClothesApiModel>>

    @DELETE(RestConstants.DELETE_CLOTHES)
    fun deleteClothes(
        @Path("clothes_id") barcode: String
    ): Single<Response<Any>>
}