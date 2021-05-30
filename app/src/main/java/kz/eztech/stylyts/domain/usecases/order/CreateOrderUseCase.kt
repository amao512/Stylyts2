package kz.eztech.stylyts.domain.usecases.order

import io.reactivex.Scheduler
import io.reactivex.Single
import kz.eztech.stylyts.data.api.RestConstants
import kz.eztech.stylyts.data.api.models.order.OrderCreateApiModel
import kz.eztech.stylyts.domain.models.order.OrderModel
import kz.eztech.stylyts.domain.repository.OrderDomainRepository
import kz.eztech.stylyts.domain.usecases.BaseUseCase
import javax.inject.Inject
import javax.inject.Named

class CreateOrderUseCase @Inject constructor(
    @Named("executor_thread") executorThread: Scheduler,
    @Named("ui_thread") uiThread: Scheduler,
    private val orderDomainRepository: OrderDomainRepository
) : BaseUseCase<OrderModel>(executorThread, uiThread) {

    private lateinit var token: String
    private lateinit var orderCreateApiModel: OrderCreateApiModel

    override fun createSingleObservable(): Single<OrderModel> {
        return orderDomainRepository.createOrder(token, orderCreateApiModel)
    }

    fun initParams(
        token: String,
        orderCreateApiModel: OrderCreateApiModel
    ) {
        this.token = RestConstants.HEADERS_AUTH_FORMAT.format(token)
        this.orderCreateApiModel = orderCreateApiModel
    }
}