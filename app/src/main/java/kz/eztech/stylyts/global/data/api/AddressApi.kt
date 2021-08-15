package kz.eztech.stylyts.global.data.api

import io.reactivex.Single
import kz.eztech.stylyts.global.data.constants.RestConstants
import kz.eztech.stylyts.global.data.models.ResultsApiModel
import kz.eztech.stylyts.global.data.models.address.AddressApiModel
import retrofit2.Response
import retrofit2.http.*

/**
 * Created by Asylzhan Seytbek on 14.03.2021.
 */
interface AddressApi {

    @FormUrlEncoded
    @POST(RestConstants.POST_ADDRESS)
    fun postAddress(
        @Field("country") country: String,
        @Field("city") city: String,
        @Field("street") street: String,
        @Field("house") house: String,
        @Field("postal_code") postalCode: String
    ) : Single<Response<AddressApiModel>>

    @GET(RestConstants.GET_ALL_ADDRESS)
    fun getAllAddress(
        @QueryMap queryMap: Map<String, String>
    ): Single<Response<ResultsApiModel<AddressApiModel>>>

    @DELETE(RestConstants.DELETE_ADDRESS)
    fun deleteAddress(
        @Path("address_id") id: String
    ): Single<Any>
}