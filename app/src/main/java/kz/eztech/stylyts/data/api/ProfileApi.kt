package kz.eztech.stylyts.data.api

import io.reactivex.Single
import kz.eztech.stylyts.domain.models.PublicationModel
import kz.eztech.stylyts.domain.models.UserModel
import kz.eztech.stylyts.domain.models.search.SearchModel
import retrofit2.Response
import retrofit2.http.*

/**
 * Created by Asylzhan Seytbek on 14.03.2021.
 */
interface ProfileApi {

    @GET(RestConstants.GET_MY_PROFILE)
    fun getMyProfile(
        @Header("Authorization") token: String
    ): Single<Response<UserModel>>

    @FormUrlEncoded
    @PATCH(RestConstants.EDIT_USER_PROFILE)
    fun editUserProfile(
        @Header("Authorization") token: String,
        @Field("name") name: String
    ): Single<Response<UserModel>>

    @GET(RestConstants.GET_USER_BY_ID)
    fun getUserProfileById(
        @Header("Authorization") token: String,
        @Path("user_id") userId: String
    ): Single<Response<UserModel>>

    @GET(RestConstants.GET_MY_POSTS)
    fun getMyCollections(
        @Header("Authorization") token: String
    ): Single<Response<SearchModel<PublicationModel>>>
}