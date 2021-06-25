package kz.eztech.stylyts.domain.usecases.search

import io.reactivex.Scheduler
import io.reactivex.Single
import kz.eztech.stylyts.data.api.RestConstants
import kz.eztech.stylyts.domain.models.clothes.ClothesModel
import kz.eztech.stylyts.domain.models.common.ResultsModel
import kz.eztech.stylyts.domain.models.common.SearchFilterModel
import kz.eztech.stylyts.domain.repository.SearchDomainRepository
import kz.eztech.stylyts.domain.usecases.BaseUseCase
import javax.inject.Inject
import javax.inject.Named

class SearchClothesUseCase @Inject constructor(
    @Named("executor_thread") executorThread: Scheduler,
    @Named("ui_thread") uiThread: Scheduler,
    private var searchDomainRepository: SearchDomainRepository
) : BaseUseCase<ResultsModel<ClothesModel>>(executorThread, uiThread) {

    private lateinit var token: String
    private lateinit var title: String
    private lateinit var map: Map<String, String>

    override fun createSingleObservable(): Single<ResultsModel<ClothesModel>> {
        return searchDomainRepository.searchClothesByTitle(token, title, map)
    }

    fun initParams(
        token: String,
        searchFilterModel: SearchFilterModel,
        page: Int
    ) {
        this.token = RestConstants.HEADERS_AUTH_FORMAT.format(token)
        this.title = searchFilterModel.query

        val map = HashMap<String, String>()
        map["page"] = page.toString()

        this.map = map
    }
}