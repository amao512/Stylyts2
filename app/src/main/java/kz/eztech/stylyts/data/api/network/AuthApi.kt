package kz.eztech.stylyts.data.api.network

import io.reactivex.Single
import kz.eztech.stylyts.data.api.RestConstants
import kz.eztech.stylyts.data.api.models.auth.AuthApiModel
import kz.eztech.stylyts.domain.models.auth.ExistsUsernameModel
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

/**
 * Created by Asylzhan Seytbek on 14.03.2021.
 */
interface AuthApi {

    @FormUrlEncoded
    @POST(RestConstants.REGISTER_USER)
    fun registerUser(
        @FieldMap fieldStringMap: Map<String, String>,
        @FieldMap fieldBooleanMap: Map<String, Boolean>
    ): Single<Response<AuthApiModel>>

    @FormUrlEncoded
    @POST(RestConstants.LOGIN_USER)
    fun loginUser(
        @Field("username") username: String,
        @Field("password") password: String
    ): Single<Response<AuthApiModel>>

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