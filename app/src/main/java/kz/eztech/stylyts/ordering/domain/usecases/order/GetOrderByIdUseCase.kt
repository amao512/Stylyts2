package kz.eztech.stylyts.ordering.domain.usecases.order

import io.reactivex.Scheduler
import io.reactivex.Single
import kz.eztech.stylyts.ordering.domain.models.order.OrderModel
import kz.eztech.stylyts.ordering.domain.repositories.OrderDomainRepository
import kz.eztech.stylyts.global.domain.usecases.BaseUseCase
import javax.inject.Inject
import javax.inject.Named

class GetOrderByIdUseCase @Inject constructor(
    @Named("executor_thread") executorThread: Scheduler,
    @Named("ui_thread") uiThread: Scheduler,
    private val orderDomainRepository: OrderDomainRepository
) : BaseUseCase<OrderModel>(executorThread, uiThread) {

    private var orderId: Int = 0

    override fun createSingleObservable(): Single<OrderModel> {
        return orderDomainRepository.getOrderById(orderId)
    }

    fun initParams(orderId: Int) {
        this.orderId = orderId
    }
}