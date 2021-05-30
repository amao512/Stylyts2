package kz.eztech.stylyts.data.api.network

import io.reactivex.Single
import kz.eztech.stylyts.data.api.RestConstants
import kz.eztech.stylyts.data.api.models.ResultsApiModel
import kz.eztech.stylyts.data.api.models.order.OrderApiModel
import kz.eztech.stylyts.data.api.models.order.OrderCreateApiModel
import retrofit2.Response
import retrofit2.http.*

interface OrderApi {

    @GET(RestConstants.GET_ORDERS)
    fun getOrderList(
        @Header("Authorization") token: String
    ): Single<Response<ResultsApiModel<OrderApiModel>>>

    @GET(RestConstants.GET_ORDER_BY_ID)
    fun getOrderById(
        @Header("Authorization") token: String,
        @Path("order_id") orderId: Int
    ): Single<Response<OrderApiModel>>

    @POST(RestConstants.CREATE_ORDER)
    fun createOrder(
        @Header("Authorization") token: String,
        @Body orderCreateApiModel: OrderCreateApiModel
    ): Single<Response<OrderApiModel>>

    @POST(RestConstants.PROCESS_ORDER)
    fun processOrder(
        @Header("Authorization") token: String,
        @Path("order_id") orderId: Int
    ): Single<Response<OrderApiModel>>
}