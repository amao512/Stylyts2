package kz.eztech.stylyts.global.domain.usecases.outfits

import io.reactivex.Scheduler
import io.reactivex.Single
import kz.eztech.stylyts.global.domain.models.outfits.OutfitModel
import kz.eztech.stylyts.global.domain.repositories.OutfitsDomainRepository
import kz.eztech.stylyts.global.domain.usecases.BaseUseCase
import javax.inject.Inject
import javax.inject.Named

class GetOutfitByIdUseCase @Inject constructor(
    @Named("executor_thread") executorThread: Scheduler,
    @Named("ui_thread") uiThread: Scheduler,
    private val outfitsDomainRepository: OutfitsDomainRepository
) : BaseUseCase<OutfitModel>(executorThread, uiThread) {

    private lateinit var outfitId: String

    override fun createSingleObservable(): Single<OutfitModel> {
        return outfitsDomainRepository.getOutfitById(outfitId)
    }

    fun initParams(outfitId: Int) {
        this.outfitId = outfitId.toString()
    }
}