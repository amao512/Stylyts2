package kz.eztech.stylyts.global.domain.usecases.main

import io.reactivex.Scheduler
import io.reactivex.Single
import kz.eztech.stylyts.global.domain.models.common.ErrorModel
import kz.eztech.stylyts.global.domain.repositories.MainDomainRepository
import kz.eztech.stylyts.global.domain.usecases.BaseUseCase
import javax.inject.Inject
import javax.inject.Named

class VerifyTokenUseCase @Inject constructor(
    @Named("executor_thread") executorThread: Scheduler,
    @Named("ui_thread") uiThread: Scheduler,
    private val mainDomainRepository: MainDomainRepository
) : BaseUseCase<ErrorModel>(executorThread, uiThread) {

    private lateinit var token: String

    override fun createSingleObservable(): Single<ErrorModel> {
        return mainDomainRepository.verifyToken(token)
    }

    fun initParams(token: String) {
        this.token = token
    }
}