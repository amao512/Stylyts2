package kz.eztech.stylyts.address.domain.usecases

import io.reactivex.Scheduler
import io.reactivex.Single
import kz.eztech.stylyts.address.domain.models.AddressModel
import kz.eztech.stylyts.address.domain.repository.AddressDomainRepository
import kz.eztech.stylyts.common.domain.usecases.BaseUseCase
import javax.inject.Inject
import javax.inject.Named

class GetAddressUseCase @Inject constructor(
    @Named("executor_thread") executorThread: Scheduler,
    @Named("ui_thread") uiThread: Scheduler,
    private val addressDomainRepository: AddressDomainRepository
) : BaseUseCase<List<AddressModel>>(executorThread, uiThread) {

    private lateinit var token: String

    override fun createSingleObservable(): Single<List<AddressModel>> {
        return addressDomainRepository.getAllAddress(token)
    }

    fun initParams(token: String) {
        this.token = "JWT $token"
    }
}