package kz.eztech.stylyts.domain.usecases.auth

import io.reactivex.Scheduler
import io.reactivex.Single
import kz.eztech.stylyts.domain.models.auth.ExistsUsernameModel
import kz.eztech.stylyts.domain.repository.AuthorizationDomainRepository
import kz.eztech.stylyts.domain.usecases.BaseUseCase
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