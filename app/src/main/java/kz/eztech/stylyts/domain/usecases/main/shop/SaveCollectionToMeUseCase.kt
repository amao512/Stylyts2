package kz.eztech.stylyts.domain.usecases.main.shop

import io.reactivex.Scheduler
import io.reactivex.Single
import kz.eztech.stylyts.domain.repository.main.ShopCategoryDomainRepository
import kz.eztech.stylyts.common.domain.BaseUseCase
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
        this.token = "Bearer $token"
        this.id = id
    }
}