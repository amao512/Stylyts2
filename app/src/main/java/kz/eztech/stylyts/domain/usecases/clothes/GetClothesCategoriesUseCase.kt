package kz.eztech.stylyts.domain.usecases.clothes

import io.reactivex.Scheduler
import io.reactivex.Single
import kz.eztech.stylyts.data.api.RestConstants
import kz.eztech.stylyts.domain.models.common.ResultsModel
import kz.eztech.stylyts.domain.models.clothes.ClothesCategoryModel
import kz.eztech.stylyts.domain.repository.ClothesDomainRepository
import kz.eztech.stylyts.domain.usecases.BaseUseCase
import javax.inject.Inject
import javax.inject.Named

/**
 * Created by Asylzhan Seytbek on 08.04.2021.
 */
class GetClothesCategoriesUseCase @Inject constructor(
    @Named("executor_thread") executorThread: Scheduler,
    @Named("ui_thread") uiThread: Scheduler,
    private val clothesDomainRepository: ClothesDomainRepository
) : BaseUseCase<ResultsModel<ClothesCategoryModel>>(executorThread, uiThread) {

    private lateinit var token: String
    private lateinit var map: Map<String, String>

    override fun createSingleObservable(): Single<ResultsModel<ClothesCategoryModel>> {
        return clothesDomainRepository.getClothesCategories(token, map)
    }

    fun initParams(
        token: String,
        typeId: Int = 0,
        page: Int = 1
    ) {
        this.token = RestConstants.HEADERS_AUTH_FORMAT.format(token)

        val map = HashMap<String, String>()
        map["page"] = page.toString()

        if (typeId != 0) {
            map["clothes_type"] = typeId.toString()
        }

        this.map = map
    }
}