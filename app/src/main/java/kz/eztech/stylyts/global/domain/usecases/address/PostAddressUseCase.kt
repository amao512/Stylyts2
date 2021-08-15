package kz.eztech.stylyts.global.domain.usecases.address

import io.reactivex.Scheduler
import io.reactivex.Single
import kz.eztech.stylyts.global.domain.models.address.AddressModel
import kz.eztech.stylyts.global.domain.repositories.AddressDomainRepository
import kz.eztech.stylyts.global.domain.usecases.BaseUseCase
import javax.inject.Inject
import javax.inject.Named

class PostAddressUseCase @Inject constructor(
    @Named("executor_thread") executorThread: Scheduler,
    @Named("ui_thread") uiThread: Scheduler,
    private val addressDomainRepository: AddressDomainRepository
) : BaseUseCase<AddressModel>(executorThread, uiThread) {

    private lateinit var data: HashMap<String, Any>

    override fun createSingleObservable(): Single<AddressModel> {
        return addressDomainRepository.postAddress(data)
    }

    fun initParams(data: HashMap<String, Any>) {
        this.data = data
    }
}