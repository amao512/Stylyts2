package kz.eztech.stylyts.constructor.domain.usecases

import io.reactivex.Scheduler
import io.reactivex.Single
import kz.eztech.stylyts.common.data.api.RestConstants
import kz.eztech.stylyts.common.domain.models.Style
import kz.eztech.stylyts.constructor.domain.repository.ShopCategoryDomainRepository
import kz.eztech.stylyts.common.domain.usecases.BaseUseCase
import javax.inject.Inject
import javax.inject.Named

/**
 * Created by Ruslan Erdenoff on 25.12.2020.
 */
class GetStylesUseCase @Inject constructor(
    @Named("executor_thread") executorThread: Scheduler,
    @Named("ui_thread") uiThread: Scheduler,
    private var shopCategoryDomainRepository: ShopCategoryDomainRepository
) : BaseUseCase<List<Style>>(executorThread, uiThread) {

    private lateinit var token: String

    override fun createSingleObservable(): Single<List<Style>> {
        return shopCategoryDomainRepository.getStyles(token)
    }

    fun initParam(token: String) {
        this.token = RestConstants.HEADERS_AUTH_FORMAT.format(token)
    }
}