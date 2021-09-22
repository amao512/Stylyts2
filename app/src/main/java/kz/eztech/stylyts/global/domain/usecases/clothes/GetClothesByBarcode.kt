package kz.eztech.stylyts.global.domain.usecases.clothes

import io.reactivex.Scheduler
import io.reactivex.Single
import kz.eztech.stylyts.global.domain.models.clothes.ClothesModel
import kz.eztech.stylyts.global.domain.repositories.ClothesDomainRepository
import kz.eztech.stylyts.global.domain.usecases.BaseUseCase
import javax.inject.Inject
import javax.inject.Named

class GetClothesByBarcode @Inject constructor(
    @Named("executor_thread") executorThread: Scheduler,
    @Named("ui_thread") uiThread: Scheduler,
    private val clothesDomainRepository: ClothesDomainRepository
) : BaseUseCase<ClothesModel>(executorThread, uiThread) {

    private lateinit var barcode: String

    override fun createSingleObservable(): Single<ClothesModel> {
        return clothesDomainRepository.getClothesByBarcode(barcode)
    }

    fun initParams(barcode: String) {
        this.barcode = barcode
    }
}