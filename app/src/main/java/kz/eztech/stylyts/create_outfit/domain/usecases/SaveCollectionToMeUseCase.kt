package kz.eztech.stylyts.create_outfit.domain.usecases

import io.reactivex.Scheduler
import io.reactivex.Single
import kz.eztech.stylyts.common.data.api.RestConstants
import kz.eztech.stylyts.create_outfit.domain.repository.ShopCategoryDomainRepository
import kz.eztech.stylyts.common.domain.usecases.BaseUseCase
import javax.inject.Inject
import javax.inject.Named

/**
 * Created by Ruslan Erdenoff on 11.02.2021.
 */
class SaveCollectionToMeUseCase @Inject constructor(
	@Named("executor_thread") executorThread: Scheduler,
	@Named("ui_thread") uiThread: Scheduler,
	private var shopCategoryDomainRepository: ShopCategoryDomainRepository
) : BaseUseCase<Unit>(executorThread, uiThread) {

    private lateinit var token: String
    private var id: Int = 0

    override fun createSingleObservable(): Single<Unit> {
        return shopCategoryDomainRepository.saveCollectionToMe(token, id)
    }

    fun initParam(token: String, id: Int) {
        this.token = RestConstants.HEADERS_AUTH_FORMAT.format(token)
        this.id = id
    }
}