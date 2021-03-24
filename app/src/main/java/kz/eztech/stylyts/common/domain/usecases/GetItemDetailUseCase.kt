package kz.eztech.stylyts.common.domain.usecases

import io.reactivex.Scheduler
import io.reactivex.Single
import kz.eztech.stylyts.common.domain.models.ClothesMainModel
import kz.eztech.stylyts.common.domain.repository.ItemDetailDomainRepository
import javax.inject.Inject
import javax.inject.Named

/**
 * Created by Ruslan Erdenoff on 28.01.2021.
 */
class GetItemDetailUseCase @Inject constructor(
    @Named("executor_thread") executorThread: Scheduler,
    @Named("ui_thread") uiThread: Scheduler,
    private var itemDetailDomainRepository: ItemDetailDomainRepository
) : BaseUseCase<ClothesMainModel>(executorThread, uiThread) {

    private lateinit var token: String
    private var id: Int = 0

    override fun createSingleObservable(): Single<ClothesMainModel> {
        return itemDetailDomainRepository.getItemDetail(token, id)
    }

    fun initParams(
        token: String,
        id: Int
    ) {
        this.token = "Bearer $token"
        this.id = id
    }
}