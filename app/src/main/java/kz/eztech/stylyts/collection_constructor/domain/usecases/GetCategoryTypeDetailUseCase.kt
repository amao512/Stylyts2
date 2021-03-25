package kz.eztech.stylyts.collection_constructor.domain.usecases

import io.reactivex.Scheduler
import io.reactivex.Single
import kz.eztech.stylyts.collection_constructor.domain.models.CategoryTypeDetailModel
import kz.eztech.stylyts.collection_constructor.domain.repository.ShopCategoryDomainRepository
import kz.eztech.stylyts.common.domain.usecases.BaseUseCase
import javax.inject.Inject
import javax.inject.Named

/**
 * Created by Ruslan Erdenoff on 18.12.2020.
 */
class GetCategoryTypeDetailUseCase @Inject constructor(
    @Named("executor_thread") executorThread: Scheduler,
    @Named("ui_thread") uiThread: Scheduler,
    private var shopCategoryDomainRepository: ShopCategoryDomainRepository
) : BaseUseCase<CategoryTypeDetailModel>(executorThread, uiThread) {

    private lateinit var data: HashMap<String, Any>

    override fun createSingleObservable(): Single<CategoryTypeDetailModel> {
        return shopCategoryDomainRepository.getCategoryDetail(data)
    }

    fun initParams(data: HashMap<String, Any>) {
        this.data = data
    }
}