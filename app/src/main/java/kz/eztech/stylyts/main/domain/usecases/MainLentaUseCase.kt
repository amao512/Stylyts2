package kz.eztech.stylyts.main.domain.usecases

import io.reactivex.Scheduler
import io.reactivex.Single
import kz.eztech.stylyts.common.domain.models.MainLentaModel
import kz.eztech.stylyts.main.domain.repository.MainLentaDomainRepository
import kz.eztech.stylyts.common.domain.usecases.BaseUseCase
import javax.inject.Inject
import javax.inject.Named

/**
 * Created by Ruslan Erdenoff on 14.01.2021.
 */
class MainLentaUseCase @Inject constructor(
    @Named("executor_thread") executorThread: Scheduler,
    @Named("ui_thread") uiThread: Scheduler,
    private var mainLentaDomainRepository: MainLentaDomainRepository
) : BaseUseCase<MainLentaModel>(executorThread, uiThread) {

    private lateinit var token: String
    private var queries: Map<String, Any>? = null

    override fun createSingleObservable(): Single<MainLentaModel> {
        return mainLentaDomainRepository.getCollections(token, queries)
    }

    fun initParams(
        token: String,
        queries: Map<String, Any>? = null
    ) {
        this.token = "Bearer $token"
        this.queries = queries
    }
}