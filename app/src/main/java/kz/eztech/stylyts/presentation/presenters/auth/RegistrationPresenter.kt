package kz.eztech.stylyts.presentation.presenters.auth

import io.reactivex.observers.DisposableSingleObserver
import kz.eztech.stylyts.data.exception.ErrorHelper
import kz.eztech.stylyts.domain.models.UserModel
import kz.eztech.stylyts.domain.usecases.auth.RegistrationUseCase
import kz.eztech.stylyts.presentation.base.processViewAction
import kz.eztech.stylyts.presentation.contracts.auth.RegistrationContract
import javax.inject.Inject

/**
 * Created by Ruslan Erdenoff on 18.12.2020.
 */
class RegistrationPresenter @Inject constructor(
    private var errorHelper: ErrorHelper,
    private var registrationUseCase: RegistrationUseCase
) : RegistrationContract.Presenter {

    private lateinit var view: RegistrationContract.View

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

    inner class RegisterUserDisposable : DisposableSingleObserver<UserModel>() {
        override fun onSuccess(t: UserModel) {
            view.processViewAction {
                hideProgress()
                processUser(t)
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