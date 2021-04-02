package kz.eztech.stylyts.domain.usecases.auth

import io.reactivex.Scheduler
import io.reactivex.Single
import kz.eztech.stylyts.domain.models.auth.TokenModel
import kz.eztech.stylyts.domain.repository.auth.AuthorizationDomainRepository
import kz.eztech.stylyts.domain.usecases.BaseUseCase
import javax.inject.Inject
import javax.inject.Named

/**
 * Created by Ruslan Erdenoff on 18.12.2020.
 */
class LoginUseCase @Inject constructor(
    @Named("executor_thread") executorThread: Scheduler,
    @Named("ui_thread") uiThread: Scheduler,
    private var authorizationDomainRepository: AuthorizationDomainRepository
) : BaseUseCase<TokenModel>(executorThread, uiThread) {

    private lateinit var data: HashMap<String, Any>

    override fun createSingleObservable(): Single<TokenModel> {
        return authorizationDomainRepository.loginUser(data)
    }

    fun initParams(data: HashMap<String, Any>) {
        this.data = data
    }
}