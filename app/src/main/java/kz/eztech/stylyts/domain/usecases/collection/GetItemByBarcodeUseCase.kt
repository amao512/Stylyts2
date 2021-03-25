package kz.eztech.stylyts.domain.usecases.collection

import io.reactivex.Scheduler
import io.reactivex.Single
import kz.eztech.stylyts.domain.models.ClothesMainModel
import kz.eztech.stylyts.domain.repository.collection.ItemDetailDomainRepository
import kz.eztech.stylyts.domain.usecases.BaseUseCase
import javax.inject.Inject
import javax.inject.Named

/**
 * Created by Ruslan Erdenoff on 24.02.2021.
 */
class GetItemByBarcodeUseCase @Inject constructor(
    @Named("executor_thread") executorThread: Scheduler,
    @Named("ui_thread") uiThread: Scheduler,
    private var itemDetailDomainRepository: ItemDetailDomainRepository
) : BaseUseCase<ClothesMainModel>(executorThread, uiThread) {

    private lateinit var token: String
    private lateinit var value: String

    override fun createSingleObservable(): Single<ClothesMainModel> {
        return itemDetailDomainRepository.getItemByBarcode(token, value)
    }

    fun initParams(
        token: String,
        value: String
    ) {
        this.token = "Bearer $token"
        this.value = value
    }
}