package kz.eztech.stylyts.data.api

import io.reactivex.Single
import kz.eztech.stylyts.domain.models.AddressModel
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
        @Field("postal_code") postalCode: String
    ) : Single<Response<AddressModel>>

    @GET(RestConstants.GET_ALL_ADDRESS)
    fun getAllAddress(
        @Header("Authorization") token: String
    ): Single<Response<List<AddressModel>>>

    @DELETE(RestConstants.DELETE_ADDRESS)
    fun deleteAddress(
        @Header("Authorization") token: String,
        @Path("address_id") id: String
    ): Single<Any>
}