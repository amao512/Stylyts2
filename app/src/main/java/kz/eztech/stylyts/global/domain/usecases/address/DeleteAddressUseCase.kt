package kz.eztech.stylyts.global.domain.usecases.address

import io.reactivex.Scheduler
import io.reactivex.Single
import kz.eztech.stylyts.global.domain.repositories.AddressDomainRepository
import kz.eztech.stylyts.global.domain.usecases.BaseUseCase
import javax.inject.Inject
import javax.inject.Named

class DeleteAddressUseCase @Inject constructor(
    @Named("executor_thread") executorThread: Scheduler,
    @Named("ui_thread") uiThread: Scheduler,
    private val addressDomainRepository: AddressDomainRepository
) : BaseUseCase<Any>(executorThread, uiThread) {

    private lateinit var addressId: String

    override fun createSingleObservable(): Single<Any> {
        return addressDomainRepository.deleteAddress(addressId)
    }

    fun initParams(
        addressId: String
    ) {
        this.addressId = addressId
    }
}