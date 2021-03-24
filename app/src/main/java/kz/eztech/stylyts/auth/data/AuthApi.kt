package kz.eztech.stylyts.auth.data

import io.reactivex.Single
import kz.eztech.stylyts.auth.domain.models.AuthModel
import kz.eztech.stylyts.common.data.api.RestConstants
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
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("name") first_name: String,
        @Field("last_name") last_name: String,
        @Field("date_of_birth") date_of_birth: String,
        @Field("should_send_mail") should_send_mail: Boolean,
        @Field("username") username: String
    ): Single<Response<AuthModel>>

    @FormUrlEncoded
    @POST(RestConstants.LOGIN_USER)
    fun loginUser(
        @Field("username") username: String,
        @Field("password") password: String
    ): Single<Response<AuthModel>>

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