package kz.eztech.stylyts.global.domain.usecases.clothes

import io.reactivex.Scheduler
import io.reactivex.Single
import kz.eztech.stylyts.global.domain.models.clothes.ClothesStyleModel
import kz.eztech.stylyts.global.domain.models.common.ResultsModel
import kz.eztech.stylyts.global.domain.repositories.ClothesDomainRepository
import kz.eztech.stylyts.global.domain.usecases.BaseUseCase
import javax.inject.Inject
import javax.inject.Named

class GetClothesStylesUseCase @Inject constructor(
    @Named("executor_thread") executorThread: Scheduler,
    @Named("ui_thread") uiThread: Scheduler,
    private val clothesDomainRepository: ClothesDomainRepository
) : BaseUseCase<ResultsModel<ClothesStyleModel>>(executorThread, uiThread) {

    private lateinit var map: Map<String, String>

    override fun createSingleObservable(): Single<ResultsModel<ClothesStyleModel>> {
        return clothesDomainRepository.getClothesStyles(map)
    }

    fun initParams(page: Int = 1) {
        val map = HashMap<String, String>()
        map["page"] = page.toString()

        this.map = map
    }
}