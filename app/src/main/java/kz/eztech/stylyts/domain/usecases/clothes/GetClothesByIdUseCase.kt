package kz.eztech.stylyts.domain.usecases.clothes

import io.reactivex.Scheduler
import io.reactivex.Single
import kz.eztech.stylyts.domain.models.clothes.ClothesModel
import kz.eztech.stylyts.domain.repository.ClothesDomainRepository
import kz.eztech.stylyts.domain.usecases.BaseUseCase
import javax.inject.Inject
import javax.inject.Named

class GetClothesByIdUseCase @Inject constructor(
    @Named("executor_thread") executorThread: Scheduler,
    @Named("ui_thread") uiThread: Scheduler,
    private val clothesDomainRepository: ClothesDomainRepository
) : BaseUseCase<ClothesModel>(executorThread, uiThread) {

    private lateinit var clothesId: String

    override fun createSingleObservable(): Single<ClothesModel> {
        return clothesDomainRepository.getClothesById(clothesId)
    }

    fun initParams(clothesId: Int) {
        this.clothesId = clothesId.toString()
    }
}