package kz.eztech.stylyts.domain.usecases.clothes

import io.reactivex.Scheduler
import io.reactivex.Single
import kz.eztech.stylyts.data.api.RestConstants
import kz.eztech.stylyts.domain.models.clothes.ClothesModel
import kz.eztech.stylyts.domain.repository.ClothesDomainRepository
import kz.eztech.stylyts.domain.usecases.BaseUseCase
import javax.inject.Inject
import javax.inject.Named

class GetClothesByBarcode @Inject constructor(
    @Named("executor_thread") executorThread: Scheduler,
    @Named("ui_thread") uiThread: Scheduler,
    private val clothesDomainRepository: ClothesDomainRepository
) : BaseUseCase<ClothesModel>(executorThread, uiThread) {

    private lateinit var token: String
    private lateinit var barcode: String

    override fun createSingleObservable(): Single<ClothesModel> {
        return clothesDomainRepository.getClothesByBarcode(token, barcode)
    }

    fun initParams(
        token: String,
        barcode: String
    ) {
        this.token = RestConstants.HEADERS_AUTH_FORMAT.format(token)
        this.barcode = barcode
    }
}