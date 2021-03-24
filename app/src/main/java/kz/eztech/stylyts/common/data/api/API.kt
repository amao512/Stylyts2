package kz.eztech.stylyts.common.data.api

import io.reactivex.Single
import kz.eztech.stylyts.common.data.api.RestConstants.GET_BRANDS
import kz.eztech.stylyts.common.data.api.RestConstants.GET_CATEGORIES
import kz.eztech.stylyts.common.data.api.RestConstants.GET_CATEGORIES_DETAIL
import kz.eztech.stylyts.common.data.api.RestConstants.GET_COLLECTIONS
import kz.eztech.stylyts.common.data.api.RestConstants.GET_FILTERED_ITEMS
import kz.eztech.stylyts.common.data.api.RestConstants.GET_ITEM_BY_BARCODE
import kz.eztech.stylyts.common.data.api.RestConstants.GET_ITEM_DETAIL
import kz.eztech.stylyts.common.data.api.RestConstants.GET_STYLES
import kz.eztech.stylyts.common.data.api.RestConstants.SAVE_COLLECTION
import kz.eztech.stylyts.common.data.api.RestConstants.SAVE_COLLECTION_TO_ME
import kz.eztech.stylyts.common.data.api.RestConstants.SAVE_ITEM_BY_PHOTO
import kz.eztech.stylyts.common.data.api.RestConstants.SEARCH_USER_BY_NAME
import kz.eztech.stylyts.common.data.api.RestConstants.UPDATE_COLLECTION
import kz.eztech.stylyts.constructor.domain.models.CategoryTypeDetailModel
import kz.eztech.stylyts.constructor.domain.models.FilteredItemsModel
import kz.eztech.stylyts.constructor.domain.models.ShopCategoryModel
import kz.eztech.stylyts.common.domain.models.ClothesMainModel
import kz.eztech.stylyts.common.domain.models.MainLentaModel
import kz.eztech.stylyts.common.domain.models.MainResult
import kz.eztech.stylyts.common.domain.models.*
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

/**
 * Created by Ruslan Erdenoff on 18.11.2020.
 */
interface API {

    @GET(GET_CATEGORIES)
    fun getCategories(): Single<Response<ShopCategoryModel>>

    @GET(GET_CATEGORIES_DETAIL)
    fun getCategoriesDetail(
        @Path("id") id: Int,
        @Query("gender") gender_type: String
    ): Single<Response<CategoryTypeDetailModel>>

    @GET(GET_STYLES)
    fun getStyles(@Header("Authorization") token: String): Single<Response<List<Style>>>

    @Multipart
    @POST(SAVE_COLLECTION)
    fun saveCollection(
        @Header("Authorization") token: String,
        //@PartMap partMap:Map<String, @JvmSuppressWildcards RequestBody> ,
        // @Part file:MultipartBody.Part ): Single<Response<Unit>>
        @Part files: ArrayList<MultipartBody.Part>
    ): Single<Response<MainResult>>

    @Multipart
    @PATCH(UPDATE_COLLECTION)
    fun updateCollection(
        @Header("Authorization") token: String,
        @Path("id") id: Int,
        @Part files: ArrayList<MultipartBody.Part>
    ): Single<Response<Unit>>
    //@POST(SAVE_COLLECTION)
    //fun saveCollection(@Header("Authorization") token: String,
    //@Body file:MultipartBody): Single<Response<Unit>>

    @GET(GET_COLLECTIONS)
    fun getCollections(
        @Header("Authorization") token: String,
        @QueryMap queries: Map<String, @JvmSuppressWildcards Any>
    ): Single<Response<MainLentaModel>>

    @GET(GET_ITEM_DETAIL)
    fun getItemDetail(
        @Header("Authorization") token: String,
        @Path("id") id: Int
    ): Single<Response<ClothesMainModel>>

    @FormUrlEncoded
    @POST(GET_ITEM_BY_BARCODE)
    fun getItemByBarcode(
        @Header("Authorization") token: String,
        @Field("barcode") value: String
    ): Single<Response<ClothesMainModel>>

    @GET(GET_FILTERED_ITEMS)
    fun getFilteredItems(
        @Header("Authorization") token: String,
        @QueryMap map: Map<String, @JvmSuppressWildcards Any>
    ): Single<Response<FilteredItemsModel>>

    @GET(GET_BRANDS)
    fun getBrands(@Header("Authorization") token: String): Single<Response<BrandsModel>>

    @GET(SAVE_COLLECTION_TO_ME)
    fun saveCollectionToMe(
        @Header("Authorization") token: String,
        @Path("id") id: Int
    ): Single<Response<Unit>>

    @GET(SEARCH_USER_BY_NAME)
    fun searchUserByUsername(
        @Header("Authorization") token: String,
        @Query("username") username: String
    ): Single<Response<List<UserSearchModel>>>

    @Multipart
    @POST(SAVE_ITEM_BY_PHOTO)
    fun saveItemByPhoto(
        @Header("Authorization") token: String,
        @Part files: ArrayList<MultipartBody.Part>
    ): Single<Response<ClothesMainModel>>
}