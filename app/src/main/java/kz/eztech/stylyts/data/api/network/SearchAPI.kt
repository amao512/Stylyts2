package kz.eztech.stylyts.data.api.network

import io.reactivex.Single
import kz.eztech.stylyts.data.api.RestConstants
import kz.eztech.stylyts.data.api.models.user.UserApiModel
import kz.eztech.stylyts.data.api.models.ResultsApiModel
import kz.eztech.stylyts.data.api.models.clothes.ClothesApiModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

/**
 * Created by Asylzhan Seytbek on 22.03.2021.
 */
interface SearchAPI {

    @GET(RestConstants.SEARCH_USER_BY_USERNAME)
    fun searchUserByUsername(
        @Header("Authorization") token: String,
        @Query("username") username: String
    ): Single<Response<ResultsApiModel<UserApiModel>>>

    @GET(RestConstants.SEARCH_CLOTHES_BY_TITLE)
    fun searchClothesByTitle(
        @Header("Authorization") token: String,
        @Query("title") title: String
    ): Single<Response<ResultsApiModel<ClothesApiModel>>>
}