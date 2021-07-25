package kz.eztech.stylyts.domain.usecases.outfits

import io.reactivex.Scheduler
import io.reactivex.Single
import kz.eztech.stylyts.domain.repository.OutfitsDomainRepository
import kz.eztech.stylyts.domain.usecases.BaseUseCase
import javax.inject.Inject
import javax.inject.Named

class DeleteOutfitUseCase @Inject constructor(
    @Named("executor_thread") executorThread: Scheduler,
    @Named("ui_thread") uiThread: Scheduler,
    private val outfitsDomainRepository: OutfitsDomainRepository
) : BaseUseCase<Any>(executorThread, uiThread) {

    private lateinit var postId: String

    override fun createSingleObservable(): Single<Any> {
        return outfitsDomainRepository.deleteOutfit(postId)
    }

    fun initParams(postId: Int) {
        this.postId = postId.toString()
    }
}