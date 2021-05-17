package kz.eztech.stylyts.domain.usecases.clothes

import io.reactivex.Scheduler
import io.reactivex.Single
import kz.eztech.stylyts.data.api.RestConstants
import kz.eztech.stylyts.domain.models.common.ResultsModel
import kz.eztech.stylyts.domain.models.clothes.ClothesBrandModel
import kz.eztech.stylyts.domain.repository.ClothesDomainRepository
import kz.eztech.stylyts.domain.usecases.BaseUseCase
import javax.inject.Inject
import javax.inject.Named

class GetClothesBrandsUseCase @Inject constructor(
    @Named("executor_thread") executorThread: Scheduler,
    @Named("ui_thread") uiThread: Scheduler,
    private val clothesDomainRepository: ClothesDomainRepository
) : BaseUseCase<ResultsModel<ClothesBrandModel>>(executorThread, uiThread) {

    private lateinit var token: String

    override fun createSingleObservable(): Single<ResultsModel<ClothesBrandModel>> {
        return clothesDomainRepository.getClothesBrands(token)
    }

    fun initParams(token: String) {
        this.token = RestConstants.HEADERS_AUTH_FORMAT.format(token)
    }
}