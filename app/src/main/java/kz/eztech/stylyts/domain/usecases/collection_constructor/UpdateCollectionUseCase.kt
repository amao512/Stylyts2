package kz.eztech.stylyts.domain.usecases.collection_constructor

import io.reactivex.Scheduler
import io.reactivex.Single
import kz.eztech.stylyts.data.api.RestConstants
import kz.eztech.stylyts.domain.repository.collection_constructor.ShopCategoryDomainRepository
import kz.eztech.stylyts.domain.usecases.BaseUseCase
import okhttp3.MultipartBody
import javax.inject.Inject
import javax.inject.Named

/**
 * Created by Ruslan Erdenoff on 04.02.2021.
 */
class UpdateCollectionUseCase @Inject constructor(
    @Named("executor_thread") executorThread: Scheduler,
    @Named("ui_thread") uiThread: Scheduler,
    private var shopCategoryDomainRepository: ShopCategoryDomainRepository
) : BaseUseCase<Unit>(executorThread, uiThread) {

    private lateinit var token: String
    private lateinit var data: ArrayList<MultipartBody.Part>
    private var id: Int = -1

    /*fun initParam(token:String,data: MultipartBody){
        this.token = "Bearer $token"
        this.data = data
    }
    override fun createSingleObservable(): Single<Unit> {
        return shopCategoryDomainRepository.saveCollection(token,data)
    }*/

    override fun createSingleObservable(): Single<Unit> {
        return shopCategoryDomainRepository.updateCollection(token, id, data)
    }

    fun initParam(
        token: String,
        id: Int,
        data: ArrayList<MultipartBody.Part>
    ) {
        this.token = RestConstants.HEADERS_AUTH_FORMAT.format(token)
        this.data = data
        this.id = id
    }
}