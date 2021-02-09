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
    private lateinit var model:Map<String,RequestBody>
    //private lateinit var data: MultipartBody.Part
    private lateinit var data:  ArrayList<MultipartBody.Part>
    @Inject
    constructor(@Named("executor_thread") executorThread: Scheduler,
                @Named("ui_thread") uiThread: Scheduler,
                shopCategoryDomainRepository: ShopCategoryDomainRepository
    ) :
            super(executorThread, uiThread) {
        this.shopCategoryDomainRepository = shopCategoryDomainRepository
    }

    /*fun initParam(token:String,data: MultipartBody){
        this.token = "Bearer $token"
        this.data = data
    }
    override fun createSingleObservable(): Single<Unit> {
        return shopCategoryDomainRepository.saveCollection(token,data)
    }*/
    fun initParam(token:String, data: ArrayList<MultipartBody.Part>){
        this.token = "Bearer $token"
        this.data = data
    }
    override fun createSingleObservable(): Single<Unit> {
        return shopCategoryDomainRepository.saveCollection(token,data)
    }
}