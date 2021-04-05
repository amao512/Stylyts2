package kz.eztech.stylyts.data.api

import io.reactivex.Single
import kz.eztech.stylyts.domain.models.UserModel
import kz.eztech.stylyts.domain.models.auth.AuthModel
import kz.eztech.stylyts.domain.models.auth.ExistsUsernameModel
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

/**
 * Created by Asylzhan Seytbek on 14.03.2021.
 */
interface AuthApi {

    @FormUrlEncoded
    @POST(RestConstants.REGISTER_USER)
    fun registerUser(
        @Field("username") username: String,
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("first_name") firstName: String,
        @Field("last_name") lastName: String,
        @Field("gender") gender: String,
        @Field("dob") dateOfBirth: String,
        @Field("is_brand") isBrand: Boolean,
    ): Single<Response<UserModel>>

    @FormUrlEncoded
    @POST(RestConstants.LOGIN_USER)
    fun loginUser(
        @Field("username") username: String,
        @Field("password") password: String
    ): Single<Response<AuthModel>>

    @FormUrlEncoded
    @POST(RestConstants.IS_USERNAME_EXISTS)
    fun isUsernameExists(
        @Field("username") username: String
    ): Single<Response<ExistsUsernameModel>>

    @FormUrlEncoded
    @POST(RestConstants.GENERATE_FORGOT_PASSWORD)
    fun generateForgotPassword(@Field("email") email: String): Single<Response<Unit>>

    @FormUrlEncoded
    @POST(RestConstants.SET_NEW_PASSWORD)
    fun setNewPassword(
        @Field("token") token: String,
        @Field("password") password: String
    ): Single<Response<Unit>>
}