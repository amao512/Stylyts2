package kz.eztech.stylyts.profile.data.api

import io.reactivex.Single
import kz.eztech.stylyts.global.data.constants.RestConstants
import kz.eztech.stylyts.global.data.models.user.UserApiModel
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

/**
 * Created by Asylzhan Seytbek on 14.03.2021.
 */
interface ProfileApi {

    @GET(RestConstants.GET_MY_PROFILE)
    fun getMyProfile(): Single<Response<UserApiModel>>

    @FormUrlEncoded
    @PATCH(RestConstants.EDIT_PROFILE)
    fun editUserProfile(
        @Field("first_name") firstName: String,
        @Field("last_name") lastName: String,
        @Field("username") username: String
//        @Field("instagram") instagram: String,
//        @Field("web_site") webSite: String
    ): Single<Response<UserApiModel>>

    @Multipart
    @PATCH(RestConstants.EDIT_PROFILE)
    fun setProfilePhoto(@Part avatar: MultipartBody.Part?): Single<Response<UserApiModel>>

    @GET(RestConstants.GET_USER_BY_ID)
    fun getUserProfileById(@Path("user_id") userId: String): Single<Response<UserApiModel>>
}