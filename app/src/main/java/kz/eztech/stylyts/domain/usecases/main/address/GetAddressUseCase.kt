package kz.eztech.stylyts.domain.usecases.main.address

import io.reactivex.Scheduler
import io.reactivex.Single
import kz.eztech.stylyts.domain.models.AddressModel
import kz.eztech.stylyts.domain.repository.main.AddressDomainRepository
import kz.eztech.stylyts.domain.usecases.BaseUseCase
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