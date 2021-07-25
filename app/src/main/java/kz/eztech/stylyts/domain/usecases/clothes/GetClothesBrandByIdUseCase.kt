package kz.eztech.stylyts.domain.usecases.clothes

import io.reactivex.Scheduler
import io.reactivex.Single
import kz.eztech.stylyts.domain.models.clothes.ClothesBrandModel
import kz.eztech.stylyts.domain.repository.ClothesDomainRepository
import kz.eztech.stylyts.domain.usecases.BaseUseCase
import javax.inject.Inject
import javax.inject.Named

class GetClothesBrandByIdUseCase @Inject constructor(
    @Named("executor_thread") executorThread: Scheduler,
    @Named("ui_thread") uiThread: Scheduler,
    private val clothesDomainRepository: ClothesDomainRepository
) : BaseUseCase<ClothesBrandModel>(executorThread, uiThread) {

    private lateinit var brandId: String

    override fun createSingleObservable(): Single<ClothesBrandModel> {
        return clothesDomainRepository.getClothesBrandById(brandId)
    }

    fun initParams(brandId: String) {
        this.brandId = brandId
    }
}