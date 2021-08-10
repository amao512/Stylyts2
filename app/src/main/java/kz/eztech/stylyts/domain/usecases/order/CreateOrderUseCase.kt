package kz.eztech.stylyts.domain.usecases.order

import io.reactivex.Scheduler
import io.reactivex.Single
import kz.eztech.stylyts.data.api.models.order.OrderCreateApiModel
import kz.eztech.stylyts.domain.models.order.ResponseOrderCreateModel
import kz.eztech.stylyts.domain.repository.OrderDomainRepository
import kz.eztech.stylyts.domain.usecases.BaseUseCase
import javax.inject.Inject
import javax.inject.Named

class CreateOrderUseCase @Inject constructor(
    @Named("executor_thread") executorThread: Scheduler,
    @Named("ui_thread") uiThread: Scheduler,
    private val orderDomainRepository: OrderDomainRepository
) : BaseUseCase<ResponseOrderCreateModel>(executorThread, uiThread) {

    private lateinit var orderCreateApiModel: OrderCreateApiModel

    override fun createSingleObservable(): Single<ResponseOrderCreateModel> {
        return orderDomainRepository.createOrder(orderCreateApiModel)
    }

    fun initParams(orderCreateApiModel: OrderCreateApiModel) {
        this.orderCreateApiModel = orderCreateApiModel
    }
}