package kz.eztech.stylyts.domain.usecases.clothes

import io.reactivex.Scheduler
import io.reactivex.Single
import kz.eztech.stylyts.domain.models.clothes.ClothesColorModel
import kz.eztech.stylyts.domain.models.common.ResultsModel
import kz.eztech.stylyts.domain.repository.ClothesDomainRepository
import kz.eztech.stylyts.domain.usecases.BaseUseCase
import javax.inject.Inject
import javax.inject.Named

class GetClothesColorsUseCase @Inject constructor(
    @Named("executor_thread") executorThread: Scheduler,
    @Named("ui_thread") uiThread: Scheduler,
    private val clothesDomainRepository: ClothesDomainRepository
) : BaseUseCase<ResultsModel<ClothesColorModel>>(executorThread, uiThread) {

    private lateinit var map: Map<String, String>

    override fun createSingleObservable(): Single<ResultsModel<ClothesColorModel>> {
        return clothesDomainRepository.getClothesColors(map)
    }

    fun initParams(page: Int = 1) {
        val map = HashMap<String, String>()
        map["page"] = page.toString()

        this.map = map
    }
}