package kz.eztech.stylyts.data.api

import io.reactivex.Single
import kz.eztech.stylyts.domain.models.SearchModel
import kz.eztech.stylyts.domain.models.UserModel
import retrofit2.Response
import retrofit2.http.*

/**
 * Created by Asylzhan Seytbek on 14.03.2021.
 */
interface UserApi {

    @GET(RestConstants.GET_USER_PROFILE)
    fun getUserProfile(
        @Header("Authorization") token: String
    ): Single<Response<UserModel>>

    @FormUrlEncoded
    @PATCH(RestConstants.EDIT_USER_PROFILE)
    fun editUserProfile(
        @Header("Authorization") token: String,
        @Field("name") name: String
    ): Single<Response<UserModel>>

    @GET(RestConstants.SEARCH_USER_BY_USERNAME)
    fun searchUserByUsername(
        @Header("Authorization") token: String,
        @Query("search") username: String
    ): Single<Response<SearchModel<UserModel>>>

    @GET(RestConstants.GET_USER_BY_ID)
    fun getUserProfileById(
        @Header("Authorization") token: String,
        @Path("user_id") userId: String
    ): Single<Response<UserModel>>
}