package kz.eztech.stylyts.domain.usecases.address

import io.reactivex.Scheduler
import io.reactivex.Single
import kz.eztech.stylyts.domain.models.address.AddressModel
import kz.eztech.stylyts.domain.models.common.ResultsModel
import kz.eztech.stylyts.domain.repository.AddressDomainRepository
import kz.eztech.stylyts.domain.usecases.BaseUseCase
import javax.inject.Inject
import javax.inject.Named

class GetAddressUseCase @Inject constructor(
    @Named("executor_thread") executorThread: Scheduler,
    @Named("ui_thread") uiThread: Scheduler,
    private val addressDomainRepository: AddressDomainRepository
) : BaseUseCase<ResultsModel<AddressModel>>(executorThread, uiThread) {

    private lateinit var queryMap: Map<String, String>

    override fun createSingleObservable(): Single<ResultsModel<AddressModel>> {
        return addressDomainRepository.getAllAddress(queryMap)
    }

    fun initParams(
        isMy: Boolean = true,
        owner: Int = 0,
        page: Int
    ) {
        val queryMap = HashMap<String, String>()

        queryMap["my"] = isMy.toString()
        queryMap["page"] = page.toString()

        if (owner != 0) {
            queryMap["user"] = owner.toString()
        }

        this.queryMap = queryMap


    }
}