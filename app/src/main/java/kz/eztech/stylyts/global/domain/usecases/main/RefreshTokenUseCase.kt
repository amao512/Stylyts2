package kz.eztech.stylyts.global.domain.usecases.main

import io.reactivex.Scheduler
import io.reactivex.Single
import kz.eztech.stylyts.auth.domain.models.TokenModel
import kz.eztech.stylyts.global.domain.repositories.MainDomainRepository
import kz.eztech.stylyts.global.domain.usecases.BaseUseCase
import javax.inject.Inject
import javax.inject.Named

class RefreshTokenUseCase @Inject constructor(
    @Named("executor_thread") executorThread: Scheduler,
    @Named("ui_thread") uiThread: Scheduler,
    private val mainDomainRepository: MainDomainRepository
) : BaseUseCase<TokenModel>(executorThread, uiThread) {

    private lateinit var refreshToken: String

    override fun createSingleObservable(): Single<TokenModel> {
        return mainDomainRepository.refreshToken(refreshToken)
    }

    fun initParams(refreshToken: String) {
        this.refreshToken = refreshToken
    }
}