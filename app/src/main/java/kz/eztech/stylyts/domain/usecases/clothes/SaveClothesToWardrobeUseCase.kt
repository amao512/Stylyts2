package kz.eztech.stylyts.domain.usecases.clothes

import io.reactivex.Scheduler
import io.reactivex.Single
import kz.eztech.stylyts.data.api.RestConstants
import kz.eztech.stylyts.domain.repository.clothes.ClothesDomainRepository
import kz.eztech.stylyts.domain.usecases.BaseUseCase
import javax.inject.Inject
import javax.inject.Named

class SaveClothesToWardrobeUseCase @Inject constructor(
    @Named("executor_thread") executorThread: Scheduler,
    @Named("ui_thread") uiThread: Scheduler,
    private val clothesDomainRepository: ClothesDomainRepository
) : BaseUseCase<Any>(executorThread, uiThread) {

    private lateinit var token: String
    private lateinit var clothesId: String

    override fun createSingleObservable(): Single<Any> {
        return clothesDomainRepository.saveClothesToWardrobe(token, clothesId)
    }

    fun initParams(
        token: String,
        clothesId: String
    ) {
        this.token = RestConstants.HEADERS_AUTH_FORMAT.format(token)
        this.clothesId = clothesId
    }
}