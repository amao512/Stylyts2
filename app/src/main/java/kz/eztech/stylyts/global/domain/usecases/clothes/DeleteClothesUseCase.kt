package kz.eztech.stylyts.global.domain.usecases.clothes

import io.reactivex.Scheduler
import io.reactivex.Single
import kz.eztech.stylyts.global.domain.repositories.ClothesDomainRepository
import kz.eztech.stylyts.global.domain.usecases.BaseUseCase
import javax.inject.Inject
import javax.inject.Named

class DeleteClothesUseCase @Inject constructor(
    @Named("executor_thread") executorThread: Scheduler,
    @Named("ui_thread") uiThread: Scheduler,
    private val clothesDomainRepository: ClothesDomainRepository
) : BaseUseCase<Any>(executorThread, uiThread) {

    private lateinit var clothesId: String

    override fun createSingleObservable(): Single<Any> {
        return clothesDomainRepository.deleteClothes(clothesId)
    }

    fun initParams(clothesId: Int) {
        this.clothesId = clothesId.toString()
    }
}