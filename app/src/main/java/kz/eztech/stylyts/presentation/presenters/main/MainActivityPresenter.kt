package kz.eztech.stylyts.presentation.presenters.main

import io.reactivex.observers.DisposableSingleObserver
import kz.eztech.stylyts.data.exception.ErrorHelper
import kz.eztech.stylyts.domain.models.ErrorModel
import kz.eztech.stylyts.domain.models.auth.TokenModel
import kz.eztech.stylyts.domain.usecases.main.RefreshTokenUseCase
import kz.eztech.stylyts.domain.usecases.main.VerifyTokenUseCase
import kz.eztech.stylyts.presentation.contracts.main.MainActivityContract
import javax.inject.Inject

class MainActivityPresenter @Inject constructor(
    private val errorHelper: ErrorHelper,
    private val verifyTokenUseCase: VerifyTokenUseCase,
    private val refreshTokenUseCase: RefreshTokenUseCase
): MainActivityContract.Presenter {

    private lateinit var view: MainActivityContract.View

    override fun disposeRequests() {
        verifyTokenUseCase.clear()
        refreshTokenUseCase.clear()
    }

    override fun attach(view: MainActivityContract.View) {
        this.view = view
    }

    override fun verifyToken(token: String) {
        verifyTokenUseCase.initParams(token)
        verifyTokenUseCase.execute(object : DisposableSingleObserver<ErrorModel>() {
            override fun onSuccess(t: ErrorModel) {
                view.processVerification(errorModel = t)
            }

            override fun onError(e: Throwable) {
                errorHelper.processError(e)
            }
        })
    }

    override fun refreshToken(refreshToken: String) {
        refreshTokenUseCase.initParams(refreshToken)
        refreshTokenUseCase.execute(object : DisposableSingleObserver<TokenModel>() {
            override fun onSuccess(t: TokenModel) {
                view.processRefresh(tokenModel = t)
            }

            override fun onError(e: Throwable) {
                errorHelper.processError(e)
            }
        })
    }
}