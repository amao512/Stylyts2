package kz.eztech.stylyts.domain.usecases.order

import io.reactivex.Scheduler
import io.reactivex.Single
import kz.eztech.stylyts.data.api.RestConstants
import kz.eztech.stylyts.domain.models.order.OrderModel
import kz.eztech.stylyts.domain.repository.OrderDomainRepository
import kz.eztech.stylyts.domain.usecases.BaseUseCase
import javax.inject.Inject
import javax.inject.Named

class ProcessOrderUseCase @Inject constructor(
    @Named("executor_thread") executorThread: Scheduler,
    @Named("ui_thread") uiThread: Scheduler,
    private val orderDomainRepository: OrderDomainRepository
) : BaseUseCase<OrderModel>(executorThread, uiThread) {

    private lateinit var token: String
    private var orderId: Int = 0

    override fun createSingleObservable(): Single<OrderModel> {
        return orderDomainRepository.processOrder(token, orderId)
    }

    fun initParams(
        token: String,
        orderId: Int
    ) {
        this.token = RestConstants.HEADERS_AUTH_FORMAT.format(token)
        this.orderId = orderId
    }
}