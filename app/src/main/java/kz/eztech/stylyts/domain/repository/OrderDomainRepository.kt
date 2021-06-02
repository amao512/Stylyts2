package kz.eztech.stylyts.domain.repository

import androidx.annotation.WorkerThread
import io.reactivex.Single
import kz.eztech.stylyts.data.api.models.order.OrderCreateApiModel
import kz.eztech.stylyts.domain.models.common.ResultsModel
import kz.eztech.stylyts.domain.models.order.OrderModel

interface OrderDomainRepository {

    @WorkerThread
    fun getOrderList(token: String): Single<ResultsModel<OrderModel>>

    @WorkerThread
    fun getOrderById(
        token: String,
        orderId: Int
    ): Single<OrderModel>

    @WorkerThread
    fun createOrder(
        token: String,
        orderCreateApiModel: OrderCreateApiModel
    ): Single<OrderModel>

    @WorkerThread
    fun processOrder(
        token: String,
        orderId: Int
    ): Single<OrderModel>
}