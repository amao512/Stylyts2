package kz.eztech.stylyts.domain.usecases.main.shop

import io.reactivex.Scheduler
import io.reactivex.Single
import kz.eztech.stylyts.domain.models.CollectionPostCreateModel
import kz.eztech.stylyts.domain.models.ExportedCollectionPostCreateModel
import kz.eztech.stylyts.domain.models.ShopCategoryModel
import kz.eztech.stylyts.domain.models.Style
import kz.eztech.stylyts.domain.repository.main.ShopCategoryDomainRepository
import kz.eztech.stylyts.domain.usecases.BaseUseCase
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject
import javax.inject.Named

/**
 * Created by Ruslan Erdenoff on 25.12.2020.
 */
class SaveCollectionConstructor:BaseUseCase<Unit> {
    private var shopCategoryDomainRepository: ShopCategoryDomainRepository
    private lateinit var token:String
    private lateinit var model:RequestBody
    //private lateinit var data: MultipartBody.Part
    private lateinit var data: MultipartBody.Part
    @Inject
    constructor(@Named("executor_thread") executorThread: Scheduler,
                @Named("ui_thread") uiThread: Scheduler,
                shopCategoryDomainRepository: ShopCategoryDomainRepository
    ) :
            super(executorThread, uiThread) {
        this.shopCategoryDomainRepository = shopCategoryDomainRepository
    }

    /*fun initParam(token:String,model: CollectionPostCreateModel,data: MultipartBody.Part){
        this.token = "Bearer $token"
        this.model = model
        this.data = data
    }
    override fun createSingleObservable(): Single<Unit> {
        return shopCategoryDomainRepository.saveCollection(token,model,data)
    }*/
    fun initParam(token:String, model: RequestBody, data: MultipartBody.Part){
        this.token = "Bearer $token"
        this.model = model
        this.data = data
    }
    override fun createSingleObservable(): Single<Unit> {
        return shopCategoryDomainRepository.saveCollection(token,model,data)
    }
}