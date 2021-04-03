package kz.eztech.stylyts.domain.usecases.address

import io.reactivex.Scheduler
import io.reactivex.Single
import kz.eztech.stylyts.data.api.RestConstants
import kz.eztech.stylyts.domain.models.address.AddressModel
import kz.eztech.stylyts.domain.repository.address.AddressDomainRepository
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
        this.token = RestConstants.HEADERS_AUTH_FORMAT.format(token)
        this.data = data
    }
}