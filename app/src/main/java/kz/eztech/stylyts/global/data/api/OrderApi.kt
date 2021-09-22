package kz.eztech.stylyts.global.data.api

import io.reactivex.Single
import kz.eztech.stylyts.global.data.constants.RestConstants
import kz.eztech.stylyts.global.data.models.ResultsApiModel
import kz.eztech.stylyts.ordering.data.models.order.OrderApiModel
import kz.eztech.stylyts.ordering.data.models.order.OrderCreateApiModel
import kz.eztech.stylyts.ordering.data.models.order.ResponseOrderCreateApiModel
import retrofit2.Response
import retrofit2.http.*

interface OrderApi {

    @GET(RestConstants.GET_ORDERS)
    fun getOrderList(
        @QueryMap queryMap: Map<String, String>
    ): Single<Response<ResultsApiModel<OrderApiModel>>>

    @GET(RestConstants.GET_ORDER_BY_ID)
    fun getOrderById(
        @Path("order_id") orderId: Int
    ): Single<Response<OrderApiModel>>

    @POST(RestConstants.CREATE_ORDER)
    fun createOrder(
        @Body orderCreateApiModel: OrderCreateApiModel
    ): Single<Response<ResponseOrderCreateApiModel>>

    @POST(RestConstants.PROCESS_ORDER)
    fun processOrder(
        @Path("order_id") orderId: Int
    ): Single<Response<OrderApiModel>>

    @FormUrlEncoded
    @POST(RestConstants.SET_DELIVERY_STATUS)
    fun setDeliveryStatus(
        @Path("order_id") orderId: Int,
        @Field("delivery_status") deliveryStatus: String
    ): Single<Response<Any>>
}