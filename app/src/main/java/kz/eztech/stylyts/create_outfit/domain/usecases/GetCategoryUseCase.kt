package kz.eztech.stylyts.create_outfit.domain.usecases

import io.reactivex.Scheduler
import io.reactivex.Single
import kz.eztech.stylyts.create_outfit.domain.models.ShopCategoryModel
import kz.eztech.stylyts.create_outfit.domain.repository.ShopCategoryDomainRepository
import kz.eztech.stylyts.common.domain.usecases.BaseUseCase
import javax.inject.Inject
import javax.inject.Named

/**
 * Created by Ruslan Erdenoff on 18.12.2020.
 */
class GetCategoryUseCase @Inject constructor(
    @Named("executor_thread") executorThread: Scheduler,
    @Named("ui_thread") uiThread: Scheduler,
    private var shopCategoryDomainRepository: ShopCategoryDomainRepository
) : BaseUseCase<ShopCategoryModel>(executorThread, uiThread) {

    override fun createSingleObservable(): Single<ShopCategoryModel> {
        return shopCategoryDomainRepository.getCategories()
    }
}