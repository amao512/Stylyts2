package kz.eztech.stylyts.data.api.network

import io.reactivex.Single
import kz.eztech.stylyts.data.api.RestConstants
import kz.eztech.stylyts.data.api.models.ResultsApiModel
import kz.eztech.stylyts.data.api.models.address.AddressApiModel
import retrofit2.Response
import retrofit2.http.*

/**
 * Created by Asylzhan Seytbek on 14.03.2021.
 */
interface AddressApi {

    @FormUrlEncoded
    @POST(RestConstants.POST_ADDRESS)
    fun postAddress(
        @Header("Authorization") token: String,
        @Field("country") country: String,
        @Field("city") city: String,
        @Field("street") street: String,
        @Field("house") house: String,
        @Field("postal_code") postalCode: String
    ) : Single<Response<AddressApiModel>>

    @GET(RestConstants.GET_ALL_ADDRESS)
    fun getAllAddress(
        @Header("Authorization") token: String,
        @QueryMap queryMap: Map<String, String>
    ): Single<Response<ResultsApiModel<AddressApiModel>>>

    @DELETE(RestConstants.DELETE_ADDRESS)
    fun deleteAddress(
        @Header("Authorization") token: String,
        @Path("address_id") id: String
    ): Single<Any>
}