package kz.eztech.stylyts.domain.usecases.clothes

import io.reactivex.Scheduler
import io.reactivex.Single
import kz.eztech.stylyts.data.api.RestConstants
import kz.eztech.stylyts.domain.models.common.ResultsModel
import kz.eztech.stylyts.domain.models.clothes.ClothesStyleModel
import kz.eztech.stylyts.domain.repository.ClothesDomainRepository
import kz.eztech.stylyts.domain.usecases.BaseUseCase
import javax.inject.Inject
import javax.inject.Named

class GetClothesStylesUseCase @Inject constructor(
    @Named("executor_thread") executorThread: Scheduler,
    @Named("ui_thread") uiThread: Scheduler,
    private val clothesDomainRepository: ClothesDomainRepository
) : BaseUseCase<ResultsModel<ClothesStyleModel>>(executorThread, uiThread) {

    private lateinit var token: String
    private lateinit var map: Map<String, String>

    override fun createSingleObservable(): Single<ResultsModel<ClothesStyleModel>> {
        return clothesDomainRepository.getClothesStyles(token, map)
    }

    fun initParams(
        token: String,
        page: Int = 1
    ) {
        this.token = RestConstants.HEADERS_AUTH_FORMAT.format(token)

        val map = HashMap<String, String>()
        map["page"] = page.toString()

        this.map = map
    }
}