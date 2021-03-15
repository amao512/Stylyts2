package kz.eztech.stylyts.domain.usecases.main.address

import io.reactivex.Scheduler
import io.reactivex.Single
import kz.eztech.stylyts.domain.models.AddressModel
import kz.eztech.stylyts.domain.repository.main.AddressDomainRepository
import kz.eztech.stylyts.domain.usecases.BaseUseCase
import javax.inject.Inject
import javax.inject.Named

class PostAddressUseCase @Inject constructor(
    @Named("executor_thread") executorThread: Scheduler,
    @Named("ui_thread") uiThread: Scheduler,
    private val addressDomainRepository: AddressDomainRepository
) : BaseUseCase<AddressModel>(executorThread, uiThread) {

    private lateinit var token: String
    private lateinit var data: HashMap<String, Any>

    override fun createSingleObservable(): Single<AddressModel> {
        return addressDomainRepository.postAddress(token, data)
    }

    fun initParams(
        token: String,
        data: HashMap<String, Any>
    ) {
        this.token = "JWT $token"
        this.data = data
    }
}