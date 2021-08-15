package kz.eztech.stylyts.auth.domain.usecases

import io.reactivex.Scheduler
import io.reactivex.Single
import kz.eztech.stylyts.auth.data.models.ExistsUsernameModel
import kz.eztech.stylyts.auth.domain.repositories.AuthorizationDomainRepository
import kz.eztech.stylyts.global.domain.usecases.BaseUseCase
import javax.inject.Inject
import javax.inject.Named

class CheckUsernameUseCase @Inject constructor(
    @Named("executor_thread") executorThread: Scheduler,
    @Named("ui_thread") uiThread: Scheduler,
    private val authorizationDomainRepository: AuthorizationDomainRepository
) : BaseUseCase<ExistsUsernameModel>(executorThread, uiThread) {

    private lateinit var username: String

    override fun createSingleObservable(): Single<ExistsUsernameModel> {
        return authorizationDomainRepository.isUsernameExists(username)
    }

    fun initParams(username: String) {
        this.username = username
    }
}