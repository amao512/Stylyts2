package kz.eztech.stylyts.domain.usecases.main

import io.reactivex.Scheduler
import io.reactivex.Single
import kz.eztech.stylyts.domain.models.auth.TokenModel
import kz.eztech.stylyts.domain.repository.MainDomainRepository
import kz.eztech.stylyts.domain.usecases.BaseUseCase
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