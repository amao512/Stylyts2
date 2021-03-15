package kz.eztech.stylyts.data.api

import io.reactivex.Single
import kz.eztech.stylyts.domain.models.ProfileModel
import retrofit2.Response
import retrofit2.http.*

/**
 * Created by Asylzhan Seytbek on 14.03.2021.
 */
interface ProfileApi {

    @GET(RestConstants.GET_USER_PROFILE)
    fun getUserProfile(
        @Header("Authorization") token: String
    ): Single<Response<List<ProfileModel>>>

    @FormUrlEncoded
    @PATCH(RestConstants.EDIT_USER_PROFILE)
    fun editUserProfile(
        @Header("Authorization") token: String,
        @Field("name") name: String
    ): Single<Response<ProfileModel>>
}