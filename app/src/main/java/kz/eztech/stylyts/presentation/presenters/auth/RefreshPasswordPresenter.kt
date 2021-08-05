package kz.eztech.stylyts.presentation.presenters.auth

import io.reactivex.observers.DisposableSingleObserver
import kz.eztech.stylyts.data.exception.ErrorHelper
import kz.eztech.stylyts.domain.usecases.auth.GenerateForgotPasswordUseCase
import kz.eztech.stylyts.domain.usecases.auth.SetNewPasswordUseCase
import kz.eztech.stylyts.presentation.base.processViewAction
import kz.eztech.stylyts.presentation.contracts.auth.RefreshPasswordContract
import kz.eztech.stylyts.presentation.fragments.auth.RefreshPasswordFragment
import kz.eztech.stylyts.presentation.utils.EMPTY_STRING
import javax.inject.Inject

/**
 * Created by Ruslan Erdenoff on 18.12.2020.
 */
class RefreshPasswordPresenter @Inject constructor(
    private var errorHelper: ErrorHelper,
    private var generateForgotPasswordUseCase: GenerateForgotPasswordUseCase,
    private var setNewPasswordUseCase: SetNewPasswordUseCase
) : RefreshPasswordContract.Presenter {

    private lateinit var view: RefreshPasswordContract.View
    private var token = EMPTY_STRING

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