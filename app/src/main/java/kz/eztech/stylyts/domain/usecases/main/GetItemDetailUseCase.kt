package kz.eztech.stylyts.domain.usecases.main

import io.reactivex.Scheduler
import io.reactivex.Single
import kz.eztech.stylyts.domain.models.ClothesMainModel
import kz.eztech.stylyts.domain.repository.main.ItemDetailDomainRepository
import kz.eztech.stylyts.domain.repository.main.ProfileDomainRepository
import kz.eztech.stylyts.domain.usecases.BaseUseCase
import javax.inject.Inject
import javax.inject.Named

/**
 * Created by Ruslan Erdenoff on 28.01.2021.
 */
class GetItemDetailUseCase:BaseUseCase<ClothesMainModel> {
    private var itemDetailDomainRepository: ItemDetailDomainRepository
    private lateinit var token:String
    private var id: Int = 0

    @Inject
    constructor(@Named("executor_thread") executorThread: Scheduler,
                @Named("ui_thread") uiThread: Scheduler,
                itemDetailDomainRepository: ItemDetailDomainRepository
    ) :
            super(executorThread, uiThread) {
        this.itemDetailDomainRepository = itemDetailDomainRepository
    }
    fun initParams(token:String,id:Int) {
        this.token = "Bearer $token"
        this.id = id
    }
    override fun createSingleObservable(): Single<ClothesMainModel> {
        return itemDetailDomainRepository.getItemDetail(token, id)
    }
}