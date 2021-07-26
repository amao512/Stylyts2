package kz.eztech.stylyts.data.repository

import android.util.Log
import io.reactivex.Single
import kz.eztech.stylyts.data.api.models.order.OrderCreateApiModel
import kz.eztech.stylyts.data.api.network.OrderApi
import kz.eztech.stylyts.data.exception.NetworkException
import kz.eztech.stylyts.presentation.utils.extensions.mappers.map
import kz.eztech.stylyts.presentation.utils.extensions.mappers.order.map
import kz.eztech.stylyts.domain.models.common.ResultsModel
import kz.eztech.stylyts.domain.models.order.OrderCreateModel
import kz.eztech.stylyts.domain.models.order.OrderModel
import kz.eztech.stylyts.domain.repository.OrderDomainRepository
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

    override fun createOrder(orderCreateApiModel: OrderCreateApiModel): Single<OrderCreateModel> {
        Log.d("TAG4", "order - $orderCreateApiModel")

        return if (orderCreateApiModel.referralUser != 0) {
            api.createOrder(
                orderCreateApiModel = orderCreateApiModel,
                referralUser = orderCreateApiModel.referralUser
            ).map {
                when (it.isSuccessful) {
                    true -> it.body().map()
                    false -> throw NetworkException(it)
                }
            }
        } else {
            api.createOrder(orderCreateApiModel).map {
                Log.d("TAG4", "${it.body()}")

                when (it.isSuccessful) {
                    true -> it.body().map()
                    false -> throw NetworkException(it)
                }
            }
        }
    }
}