package kz.eztech.stylyts.domain.usecases.outfits

import io.reactivex.Scheduler
import io.reactivex.Single
import kz.eztech.stylyts.data.api.RestConstants
import kz.eztech.stylyts.domain.models.outfits.OutfitModel
import kz.eztech.stylyts.domain.repository.outfits.OutfitsDomainRepository
import kz.eztech.stylyts.domain.usecases.BaseUseCase
import javax.inject.Inject
import javax.inject.Named

class GetOutfitByIdUseCase @Inject constructor(
    @Named("executor_thread") executorThread: Scheduler,
    @Named("ui_thread") uiThread: Scheduler,
    private val outfitsDomainRepository: OutfitsDomainRepository
) : BaseUseCase<OutfitModel>(executorThread, uiThread) {

    private lateinit var token: String
    private lateinit var outfitId: String

    override fun createSingleObservable(): Single<OutfitModel> {
        return outfitsDomainRepository.getOutfitById(token, outfitId)
    }

    fun initParams(
        token: String,
        outfitId: String
    ) {
        this.token = RestConstants.HEADERS_AUTH_FORMAT.format(token)
        this.outfitId = outfitId
    }
}