package kz.eztech.stylyts.domain.usecases.collection_constructor

import io.reactivex.Scheduler
import io.reactivex.Single
import kz.eztech.stylyts.data.api.RestConstants
import kz.eztech.stylyts.domain.models.BrandsModel
import kz.eztech.stylyts.domain.repository.collection_constructor.ShopCategoryDomainRepository
import kz.eztech.stylyts.domain.usecases.BaseUseCase
import javax.inject.Inject
import javax.inject.Named

/**
 * Created by Ruslan Erdenoff on 05.02.2021.
 */
class GetBrandsUseCase @Inject constructor(
    @Named("executor_thread") executorThread: Scheduler,
    @Named("ui_thread") uiThread: Scheduler,
    private var shopCategoryDomainRepository: ShopCategoryDomainRepository
) : BaseUseCase<BrandsModel>(executorThread, uiThread) {

    private lateinit var token: String

    override fun createSingleObservable(): Single<BrandsModel> {
        return shopCategoryDomainRepository.getBrands(token)
    }

    fun initParam(token: String) {
        this.token = RestConstants.HEADERS_AUTH_FORMAT.format(token)
    }
}