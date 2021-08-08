package kz.eztech.stylyts.domain.usecases.order

import io.reactivex.Scheduler
import io.reactivex.Single
import kz.eztech.stylyts.domain.repository.OrderDomainRepository
import kz.eztech.stylyts.domain.usecases.BaseUseCase
import kz.eztech.stylyts.presentation.enums.ordering.DeliveryStatusEnum
import javax.inject.Inject
import javax.inject.Named

class SetDeliveryStatusUseCase @Inject constructor(
    @Named("executor_thread") executorThread: Scheduler,
    @Named("ui_thread") uiThread: Scheduler,
    private val orderDomainRepository: OrderDomainRepository
) : BaseUseCase<Any>(executorThread, uiThread) {

    private var orderId: Int = 0
    private lateinit var deliveryStatus: String

    override fun createSingleObservable(): Single<Any> {
        return orderDomainRepository.setOrderStatus(orderId, deliveryStatus)
    }

    fun setStatusInProgress(orderId: Int) {
        this.orderId = orderId
        this.deliveryStatus = DeliveryStatusEnum.IN_PROGRESS.status
    }

    fun setStatusDelivered(orderId: Int) {
        this.orderId = orderId
        this.deliveryStatus = DeliveryStatusEnum.DELIVERED.status
    }
}