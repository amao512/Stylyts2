package kz.eztech.stylyts.ordering.data.repositories

import io.reactivex.Single
import kz.eztech.stylyts.ordering.data.models.order.OrderCreateApiModel
import kz.eztech.stylyts.global.data.api.OrderApi
import kz.eztech.stylyts.global.data.exception.NetworkException
import kz.eztech.stylyts.global.domain.models.common.ResultsModel
import kz.eztech.stylyts.ordering.domain.models.order.OrderModel
import kz.eztech.stylyts.ordering.domain.models.order.ResponseOrderCreateModel
import kz.eztech.stylyts.ordering.domain.repositories.OrderDomainRepository
import kz.eztech.stylyts.utils.mappers.map
import kz.eztech.stylyts.utils.mappers.order.map
import javax.inject.Inject

class OrderRepository @Inject constructor(
    private val api: OrderApi
) : OrderDomainRepository {

    override fun getOrderList(queryMap: Map<String, String>): Single<ResultsModel<OrderModel>> {
        return api.getOrderList(queryMap).map {
            when (it.isSuccessful) {
                true -> it.body().map()
                false -> throw NetworkException(it)
            }
        }
    }

    override fun getOrderById(orderId: Int): Single<OrderModel> {
        return api.getOrderById(orderId).map {
            when (it.isSuccessful) {
                true -> it.body().map()
                false -> throw NetworkException(it)
            }
        }
    }

    override fun createOrder(orderCreateApiModel: OrderCreateApiModel): Single<ResponseOrderCreateModel> {
        return api.createOrder(orderCreateApiModel).map {
            when (it.isSuccessful) {
                true -> it.body().map()
                false -> throw NetworkException(it)
            }
        }
    }

    override fun setOrderStatus(
        orderId: Int,
        deliveryStatus: String
    ): Single<Any> {
        return api.setDeliveryStatus(
            orderId = orderId,
            deliveryStatus = deliveryStatus
        ).map {
            when (it.isSuccessful) {
                true -> it.body()
                false -> throw NetworkException(it)
            }
        }
    }
}