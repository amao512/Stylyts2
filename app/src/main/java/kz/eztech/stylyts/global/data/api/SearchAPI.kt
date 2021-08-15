package kz.eztech.stylyts.global.data.api

import io.reactivex.Single
import kz.eztech.stylyts.global.data.constants.RestConstants
import kz.eztech.stylyts.global.data.models.ResultsApiModel
import kz.eztech.stylyts.global.data.models.clothes.ClothesApiModel
import kz.eztech.stylyts.global.data.models.user.UserApiModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.QueryMap

/**
 * Created by Asylzhan Seytbek on 22.03.2021.
 */
interface SearchAPI {

    @GET(RestConstants.SEARCH_USER_BY_USERNAME)
    fun searchUserByUsername(
        @Query("username") username: String,
        @QueryMap queryMap: Map<String, String>
    ): Single<Response<ResultsApiModel<UserApiModel>>>

    @GET(RestConstants.SEARCH_CLOTHES_BY_TITLE)
    fun searchClothesByTitle(
        @Query("title") title: String,
        @QueryMap queryMap: Map<String, String>
    ): Single<Response<ResultsApiModel<ClothesApiModel>>>
}