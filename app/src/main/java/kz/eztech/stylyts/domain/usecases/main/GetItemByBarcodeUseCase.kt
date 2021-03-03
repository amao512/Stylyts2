package kz.eztech.stylyts.domain.usecases.main

import io.reactivex.Scheduler
import io.reactivex.Single
import kz.eztech.stylyts.domain.models.ClothesMainModel
import kz.eztech.stylyts.domain.repository.main.ItemDetailDomainRepository
import kz.eztech.stylyts.domain.usecases.BaseUseCase
import javax.inject.Inject
import javax.inject.Named

/**
 * Created by Ruslan Erdenoff on 24.02.2021.
 */
class GetItemByBarcodeUseCase:BaseUseCase<ClothesMainModel> {

    private var itemDetailDomainRepository: ItemDetailDomainRepository
    private lateinit var token:String
    private lateinit var value: String

    @Inject
    constructor(@Named("executor_thread") executorThread: Scheduler,
                @Named("ui_thread") uiThread: Scheduler,
                itemDetailDomainRepository: ItemDetailDomainRepository
    ) :
            super(executorThread, uiThread) {
        this.itemDetailDomainRepository = itemDetailDomainRepository
    }
    fun initParams(token:String,value:String) {
        this.token = "Bearer $token"
        this.value = value
    }
    override fun createSingleObservable(): Single<ClothesMainModel> {
        return itemDetailDomainRepository.getItemByBarcode(token, value)
    }
}