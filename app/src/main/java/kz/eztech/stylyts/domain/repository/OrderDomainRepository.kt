package kz.eztech.stylyts.domain.repository

import io.reactivex.Single
import kz.eztech.stylyts.data.api.models.order.OrderCreateApiModel
import kz.eztech.stylyts.domain.models.common.ResultsModel
import kz.eztech.stylyts.domain.models.order.OrderCreateModel
import kz.eztech.stylyts.domain.models.order.OrderModel

interface OrderDomainRepository {

    fun getOrderList(queryMap: Map<String, String>): Single<ResultsModel<OrderModel>>

    fun getOrderById(orderId: Int): Single<OrderModel>

    fun createOrder(orderCreateApiModel: OrderCreateApiModel): Single<OrderCreateModel>
}