package kz.eztech.stylyts.domain.usecases.clothes

import io.reactivex.Scheduler
import io.reactivex.Single
import kz.eztech.stylyts.domain.models.clothes.ClothesTypeModel
import kz.eztech.stylyts.domain.models.common.ResultsModel
import kz.eztech.stylyts.domain.repository.ClothesDomainRepository
import kz.eztech.stylyts.domain.usecases.BaseUseCase
import javax.inject.Inject
import javax.inject.Named

/**
 * Created by Asylzhan Seytbek on 06.04.2021.
 */
class GetClothesTypesUseCase @Inject constructor(
    @Named("executor_thread") executorThread: Scheduler,
    @Named("ui_thread") uiThread: Scheduler,
    private val clothesDomainRepository: ClothesDomainRepository
) : BaseUseCase<ResultsModel<ClothesTypeModel>>(executorThread, uiThread) {

    private lateinit var map: Map<String, String>

    override fun createSingleObservable(): Single<ResultsModel<ClothesTypeModel>> {
        return clothesDomainRepository.getClothesTypes(map)
    }

    fun initParams(page: Int = 1) {
        val map = HashMap<String, String>()
        map["page"] = page.toString()

        this.map = map
    }
}