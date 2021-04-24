package kz.eztech.stylyts.domain.usecases.outfits

import io.reactivex.Scheduler
import io.reactivex.Single
import kz.eztech.stylyts.data.api.RestConstants
import kz.eztech.stylyts.domain.repository.outfits.OutfitsDomainRepository
import kz.eztech.stylyts.domain.usecases.BaseUseCase
import javax.inject.Inject
import javax.inject.Named

class DeleteOutfitUseCase @Inject constructor(
    @Named("executor_thread") executorThread: Scheduler,
    @Named("ui_thread") uiThread: Scheduler,
    private val outfitsDomainRepository: OutfitsDomainRepository
) : BaseUseCase<Any>(executorThread, uiThread) {

    private lateinit var token: String
    private lateinit var postId: String

    override fun createSingleObservable(): Single<Any> {
        return outfitsDomainRepository.deleteOutfit(token, postId)
    }

    fun initParams(
        token: String,
        postId: Int
    ) {
        this.token = RestConstants.HEADERS_AUTH_FORMAT.format(token)
        this.postId = postId.toString()
    }
}