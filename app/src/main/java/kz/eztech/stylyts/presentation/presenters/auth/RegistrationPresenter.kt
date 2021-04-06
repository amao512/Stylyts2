package kz.eztech.stylyts.presentation.presenters.auth

import io.reactivex.observers.DisposableSingleObserver
import kz.eztech.stylyts.data.exception.ErrorHelper
import kz.eztech.stylyts.domain.models.auth.AuthModel
import kz.eztech.stylyts.domain.models.auth.ExistsUsernameModel
import kz.eztech.stylyts.domain.usecases.auth.CheckUsernameUseCase
import kz.eztech.stylyts.domain.usecases.auth.RegistrationUseCase
import kz.eztech.stylyts.presentation.base.processViewAction
import kz.eztech.stylyts.presentation.contracts.auth.RegistrationContract
import javax.inject.Inject

/**
 * Created by Ruslan Erdenoff on 18.12.2020.
 */
class RegistrationPresenter @Inject constructor(
    private var errorHelper: ErrorHelper,
    private var registrationUseCase: RegistrationUseCase,
    private val checkUsernameUseCase: CheckUsernameUseCase
) : RegistrationContract.Presenter {

    private lateinit var view: RegistrationContract.View

    override fun checkUsername(username: String) {
        checkUsernameUseCase.initParams(username)
        checkUsernameUseCase.execute(object : DisposableSingleObserver<ExistsUsernameModel>() {
            override fun onSuccess(t: ExistsUsernameModel) {
                view.processViewAction {
                    isUsernameExists(existsUsernameModel = t)
                }
            }

            override fun onError(e: Throwable) {
                view.processViewAction {
                    displayMessage(msg = errorHelper.processError(e))
                }
            }
        })
    }

    override fun registerUser(data: HashMap<String, Any>) {
        view.displayProgress()
        registrationUseCase.initParams(data)
        registrationUseCase.execute(RegisterUserDisposable())
    }

    override fun disposeRequests() {
        registrationUseCase.clear()
    }

    override fun attach(view: RegistrationContract.View) {
        this.view = view
    }

    inner class RegisterUserDisposable : DisposableSingleObserver<AuthModel>() {
        override fun onSuccess(t: AuthModel) {
            view.processViewAction {
                hideProgress()
                processUser(authModel = t)
            }
        }

        override fun onError(e: Throwable) {
            view.processViewAction {
                hideProgress()
                displayMessage(errorHelper.processError(e))
            }
        }
    }
}