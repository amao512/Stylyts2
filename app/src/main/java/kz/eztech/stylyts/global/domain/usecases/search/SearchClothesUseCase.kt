package kz.eztech.stylyts.global.domain.usecases.search

import io.reactivex.Scheduler
import io.reactivex.Single
import kz.eztech.stylyts.global.domain.models.clothes.ClothesModel
import kz.eztech.stylyts.global.domain.models.common.ResultsModel
import kz.eztech.stylyts.global.domain.models.common.SearchFilterModel
import kz.eztech.stylyts.global.domain.repositories.SearchDomainRepository
import kz.eztech.stylyts.global.domain.usecases.BaseUseCase
import javax.inject.Inject
import javax.inject.Named

class SearchClothesUseCase @Inject constructor(
    @Named("executor_thread") executorThread: Scheduler,
    @Named("ui_thread") uiThread: Scheduler,
    private var searchDomainRepository: SearchDomainRepository
) : BaseUseCase<ResultsModel<ClothesModel>>(executorThread, uiThread) {

    private lateinit var title: String
    private lateinit var map: Map<String, String>

    override fun createSingleObservable(): Single<ResultsModel<ClothesModel>> {
        return searchDomainRepository.searchClothesByTitle(title, map)
    }

    fun initParams(
        searchFilterModel: SearchFilterModel,
        page: Int
    ) {
        this.title = searchFilterModel.query

        val map = HashMap<String, String>()
        map["page"] = page.toString()

        this.map = map
    }
}