package kz.eztech.stylyts.auth.presentation.presenters

import io.reactivex.observers.DisposableSingleObserver
import kz.eztech.stylyts.data.exception.ErrorHelper
import kz.eztech.stylyts.auth.domain.usecases.GenerateForgotPasswordUseCase
import kz.eztech.stylyts.auth.domain.usecases.SetNewPasswordUseCase
import kz.eztech.stylyts.common.presentation.base.processViewAction
import kz.eztech.stylyts.auth.presentation.contracts.RefreshPasswordContract
import kz.eztech.stylyts.auth.presentation.fragments.RefreshPasswordFragment
import javax.inject.Inject

/**
 * Created by Ruslan Erdenoff on 18.12.2020.
 */
class RefreshPasswordPresenter: RefreshPasswordContract.Presenter {
    private var errorHelper: ErrorHelper
    private var generateForgotPasswordUseCase: GenerateForgotPasswordUseCase
    private var setNewPasswordUseCase: SetNewPasswordUseCase
    private lateinit var view: RefreshPasswordContract.View
    private var token =""

    @Inject
    constructor(errorHelper: ErrorHelper,
                generateForgotPasswordUseCase: GenerateForgotPasswordUseCase,
                setNewPasswordUseCase: SetNewPasswordUseCase,
    ){
        this.generateForgotPasswordUseCase = generateForgotPasswordUseCase
        this.setNewPasswordUseCase = setNewPasswordUseCase
        this.errorHelper = errorHelper
    }
    override fun disposeRequests() {
        generateForgotPasswordUseCase.clear()
        setNewPasswordUseCase.clear()
    }

    override fun attach(view: RefreshPasswordContract.View) {
        this.view = view
    }

    override fun generateForgotPassword(email: String) {
        view.displayProgress()
        generateForgotPasswordUseCase.initParams(email)
        generateForgotPasswordUseCase.execute(object : DisposableSingleObserver<Unit>(){
            override fun onSuccess(t: Unit) {
                view.processViewAction {
                    changeState(RefreshPasswordFragment.RefreshStates.REFRESH_PASSWORD)
                    hideProgress()
                }
            }

            override fun onError(e: Throwable) {
                view.processViewAction {
                    changeState(RefreshPasswordFragment.RefreshStates.REFRESH_PASSWORD)
                    hideProgress()
                    displayMessage(errorHelper.processError(e))
                }
            }
        })
    }

    override fun setNewPassword(password: String) {
        view.displayProgress()
        val data = HashMap<String,Any>()
        data["token"] = token
        data["password"] = password
        setNewPasswordUseCase.initParams(data)
        setNewPasswordUseCase.execute(object : DisposableSingleObserver<Unit>(){
            override fun onSuccess(t: Unit) {
                view.processViewAction {
                    changeState(RefreshPasswordFragment.RefreshStates.COMPLETE)
                    hideProgress()
                }
            }

            override fun onError(e: Throwable) {
                view.processViewAction {
                    changeState(RefreshPasswordFragment.RefreshStates.COMPLETE)
                    hideProgress()
                    displayMessage(errorHelper.processError(e))
                }

            }
        })
    }
}