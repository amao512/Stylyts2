package kz.eztech.stylyts.domain.usecases.outfits

import io.reactivex.Scheduler
import io.reactivex.Single
import kz.eztech.stylyts.data.api.RestConstants
import kz.eztech.stylyts.domain.models.ResultsModel
import kz.eztech.stylyts.domain.models.outfits.OutfitModel
import kz.eztech.stylyts.domain.repository.outfits.OutfitsDomainRepository
import kz.eztech.stylyts.domain.usecases.BaseUseCase
import javax.inject.Inject
import javax.inject.Named

class GetOutfitsUseCase @Inject constructor(
    @Named("executor_thread") executorThread: Scheduler,
    @Named("ui_thread") uiThread: Scheduler,
    private val outfitsDomainRepository: OutfitsDomainRepository
) : BaseUseCase<ResultsModel<OutfitModel>>(executorThread, uiThread) {

    private lateinit var token: String
    private lateinit var booleanQueryMap: Map<String, Boolean>

    override fun createSingleObservable(): Single<ResultsModel<OutfitModel>> {
        return outfitsDomainRepository.getOutfits(token, booleanQueryMap)
    }

    fun initParams(
        token: String,
        gender: String = "M",
        isMy: Boolean = false
    ) {
        this.token = RestConstants.HEADERS_AUTH_FORMAT.format(token)
        val booleanQueryMap: MutableMap<String, Boolean> = HashMap()



        if (isMy) {
            booleanQueryMap["my"] = true
        }

        this.booleanQueryMap = booleanQueryMap
    }
}