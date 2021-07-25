package kz.eztech.stylyts.domain.usecases.order

import io.reactivex.Scheduler
import io.reactivex.Single
import kz.eztech.stylyts.domain.models.common.ResultsModel
import kz.eztech.stylyts.domain.models.order.OrderModel
import kz.eztech.stylyts.domain.repository.OrderDomainRepository
import kz.eztech.stylyts.domain.usecases.BaseUseCase
import javax.inject.Inject
import javax.inject.Named

class GetOrderListUseCase @Inject constructor(
    @Named("executor_thread") executorThread: Scheduler,
    @Named("ui_thread") uiThread: Scheduler,
    private val orderDomainRepository: OrderDomainRepository
) : BaseUseCase<ResultsModel<OrderModel>>(executorThread, uiThread) {

    private lateinit var queryMap: Map<String, String>

    override fun createSingleObservable(): Single<ResultsModel<OrderModel>> {
        return orderDomainRepository.getOrderList(queryMap)
    }

    fun initParams(page: Int) {
        val queryMap = HashMap<String, String>()

        queryMap["page"] = page.toString()

        this.queryMap = queryMap
    }
}