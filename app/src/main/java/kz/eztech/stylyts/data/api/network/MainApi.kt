package kz.eztech.stylyts.data.api.network

import io.reactivex.Single
import kz.eztech.stylyts.data.api.RestConstants
import kz.eztech.stylyts.domain.models.ErrorModel
import kz.eztech.stylyts.domain.models.auth.TokenModel
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
    ): Single<Response<TokenModel>>
}