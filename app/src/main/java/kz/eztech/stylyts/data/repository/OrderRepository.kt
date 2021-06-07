package kz.eztech.stylyts.data.repository

import io.reactivex.Single
import kz.eztech.stylyts.data.api.models.order.OrderCreateApiModel
import kz.eztech.stylyts.data.api.network.OrderApi
import kz.eztech.stylyts.data.exception.NetworkException
import kz.eztech.stylyts.data.mappers.ResultsApiModelMapper
import kz.eztech.stylyts.data.mappers.order.OrderApiModelMapper
import kz.eztech.stylyts.data.mappers.order.OrderCreateApiModelMapper
import kz.eztech.stylyts.domain.models.common.ResultsModel
import kz.eztech.stylyts.domain.models.order.OrderCreateModel
import kz.eztech.stylyts.domain.models.order.OrderModel
import kz.eztech.stylyts.domain.repository.OrderDomainRepository
import javax.inject.Inject

class OrderRepository @Inject constructor(
    private val api: OrderApi,
    private val resultsApiModelMapper: ResultsApiModelMapper,
    private val orderApiModelMapper: OrderApiModelMapper,
    private val orderCreateApiModelMapper: OrderCreateApiModelMapper
) : OrderDomainRepository {

    override fun getOrderList(
        token: String,
        queryMap: Map<String, String>
    ): Single<ResultsModel<OrderModel>> {
        return api.getOrderList(token, queryMap).map {
            when (it.isSuccessful) {
                true -> resultsApiModelMapper.map(data = it.body())
                false -> throw NetworkException(it)
            }
        }
    }

    override fun getOrderById(
        token: String,
        orderId: Int
    ): Single<OrderModel> {
        return api.getOrderById(token, orderId).map {
            when (it.isSuccessful) {
                true -> orderApiModelMapper.map(data = it.body())
                false -> throw NetworkException(it)
            }
        }
    }

    override fun createOrder(
        token: String,
        orderCreateApiModel: OrderCreateApiModel
    ): Single<OrderCreateModel> {
        return api.createOrder(token, orderCreateApiModel).map {
            when (it.isSuccessful) {
                true -> orderCreateApiModelMapper.map(data = it.body())
                false -> throw NetworkException(it)
            }
        }
    }
}