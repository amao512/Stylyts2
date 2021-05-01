package kz.eztech.stylyts.domain.usecases.address

import io.reactivex.Scheduler
import io.reactivex.Single
import kz.eztech.stylyts.domain.repository.AddressDomainRepository
import kz.eztech.stylyts.domain.usecases.BaseUseCase
import javax.inject.Inject
import javax.inject.Named

class DeleteAddressUseCase @Inject constructor(
    @Named("executor_thread") executorThread: Scheduler,
    @Named("ui_thread") uiThread: Scheduler,
    private val addressDomainRepository: AddressDomainRepository
) : BaseUseCase<Any>(executorThread, uiThread) {

    private lateinit var token: String
    private lateinit var addressId: String

    override fun createSingleObservable(): Single<Any> {
        return addressDomainRepository.deleteAddress(token, addressId)
    }

    fun initParams(
        token: String,
        addressId: String
    ) {
        this.token = "JWT $token"
        this.addressId = addressId
    }
}