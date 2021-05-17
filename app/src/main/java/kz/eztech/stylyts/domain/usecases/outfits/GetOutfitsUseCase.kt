package kz.eztech.stylyts.domain.usecases.outfits

import io.reactivex.Scheduler
import io.reactivex.Single
import kz.eztech.stylyts.data.api.RestConstants
import kz.eztech.stylyts.domain.models.common.ResultsModel
import kz.eztech.stylyts.domain.models.filter.FilterModel
import kz.eztech.stylyts.domain.models.outfits.OutfitModel
import kz.eztech.stylyts.domain.repository.OutfitsDomainRepository
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
    private lateinit var stringQueryMap: Map<String, String>

    override fun createSingleObservable(): Single<ResultsModel<OutfitModel>> {
        return outfitsDomainRepository.getOutfits(
            token = token,
            booleanQueryMap = booleanQueryMap,
            stringQueryMap = stringQueryMap
        )
    }

    fun initParams(
        token: String,
        filterModel: FilterModel
    ) {
        this.token = RestConstants.HEADERS_AUTH_FORMAT.format(token)

        val booleanQueryMap = HashMap<String, Boolean>()
        val stringQueryMap = HashMap<String, String>()

        if (filterModel.userId != 0) {
            stringQueryMap["author"] = filterModel.userId.toString()
        }

        if (filterModel.page != 0) {
            stringQueryMap["page"] = filterModel.page.toString()
        }

        if (filterModel.isMy) {
            booleanQueryMap["my"] = filterModel.isMy
        }

        if (filterModel.minPrice != 0) {
            stringQueryMap["total_price_gt"] = filterModel.minPrice.toString()
        }

        if (filterModel.maxPrice != 0) {
            stringQueryMap["total_price_lt"] = filterModel.minPrice.toString()
        }

        stringQueryMap["gender"] = filterModel.gender


        this.booleanQueryMap = booleanQueryMap
        this.stringQueryMap = stringQueryMap
    }
}