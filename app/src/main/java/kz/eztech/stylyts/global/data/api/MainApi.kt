package kz.eztech.stylyts.global.data.api

import io.reactivex.Single
import kz.eztech.stylyts.global.data.constants.RestConstants
import kz.eztech.stylyts.global.domain.models.common.ErrorModel
import kz.eztech.stylyts.auth.data.models.TokenApiModel
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface MainApi {

    @FormUrlEncoded
    @POST(RestConstants.VERIFY_TOKEN)
    fun verifyToken(
        @Field("token") token: String
    ): Single<Response<ErrorModel>>

    @FormUrlEncoded
    @POST(RestConstants.REFRESH_TOKEN)
    fun refreshToken(
        @Field("refresh") refresh: String
    ): Single<Response<TokenApiModel>>
}