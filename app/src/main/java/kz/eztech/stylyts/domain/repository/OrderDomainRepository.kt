package kz.eztech.stylyts.domain.repository

import io.reactivex.Single
import kz.eztech.stylyts.data.api.models.order.OrderCreateApiModel
import kz.eztech.stylyts.domain.models.common.ResultsModel
import kz.eztech.stylyts.domain.models.order.OrderModel
import kz.eztech.stylyts.domain.models.order.ResponseOrderCreateModel

interface OrderDomainRepository {

    fun getOrderList(queryMap: Map<String, String>): Single<ResultsModel<OrderModel>>

    fun getOrderById(orderId: Int): Single<OrderModel>

    fun createOrder(orderCreateApiModel: OrderCreateApiModel): Single<ResponseOrderCreateModel>

    fun setOrderStatus(
        orderId: Int,
        deliveryStatus: String
    ): Single<Any>
}