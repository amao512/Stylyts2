package kz.eztech.stylyts.domain.usecases

import io.reactivex.Scheduler
import io.reactivex.Single
import kz.eztech.stylyts.domain.repository.AuthorizationDomainRepository
import javax.inject.Inject
import javax.inject.Named

/**
 * Created by Ruslan Erdenoff on 18.12.2020.
 */
class GenerateForgotPasswordUseCase:BaseUseCase<Unit> {
    private var authorizationDomainRepository: AuthorizationDomainRepository
    private lateinit var email:String
    @Inject
    constructor(@Named("executor_thread") executorThread: Scheduler,
                @Named("ui_thread") uiThread: Scheduler,
                authorizationDomainRepository: AuthorizationDomainRepository
    ) :
            super(executorThread, uiThread) {
        this.authorizationDomainRepository = authorizationDomainRepository
    }
    fun initParams(email:String) {
        this.email = email
    }

    override fun createSingleObservable(): Single<Unit> {
        return authorizationDomainRepository.generateForgotPassword(email)
    }
}