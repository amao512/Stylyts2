package kz.eztech.stylyts.search.data

import io.reactivex.Single
import kz.eztech.stylyts.common.data.api.RestConstants
import kz.eztech.stylyts.common.domain.models.UserModel
import kz.eztech.stylyts.search.domain.models.SearchModel
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
        @Query("search") username: String
    ): Single<Response<SearchModel<UserModel>>>
}