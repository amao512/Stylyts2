package kz.eztech.stylyts.auth.domain.usecases

import io.reactivex.Scheduler
import io.reactivex.Single
import kz.eztech.stylyts.auth.domain.repository.AuthorizationDomainRepository
import kz.eztech.stylyts.common.domain.usecases.BaseUseCase
import javax.inject.Inject
import javax.inject.Named

/**
 * Created by Ruslan Erdenoff on 18.12.2020.
 */
class SetNewPasswordUseCase @Inject constructor(
    @Named("executor_thread") executorThread: Scheduler,
    @Named("ui_thread") uiThread: Scheduler,
    private var authorizationDomainRepository: AuthorizationDomainRepository
) : BaseUseCase<Unit>(executorThread, uiThread) {

    private lateinit var data: HashMap<String, Any>

    override fun createSingleObservable(): Single<Unit> {
        return authorizationDomainRepository.setNewPassword(data)
    }

    fun initParams(data: HashMap<String, Any>) {
        this.data = data
    }
}