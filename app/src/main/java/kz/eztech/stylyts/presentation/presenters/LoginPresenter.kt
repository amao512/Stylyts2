package kz.eztech.stylyts.presentation.presenters

import io.reactivex.observers.DisposableSingleObserver
import kz.eztech.stylyts.data.exception.ErrorHelper
import kz.eztech.stylyts.domain.models.UserModel
import kz.eztech.stylyts.domain.usecases.LoginUseCase
import kz.eztech.stylyts.domain.usecases.RegistrationUseCase
import kz.eztech.stylyts.presentation.base.BasePresenter
import kz.eztech.stylyts.presentation.base.processViewAction
import kz.eztech.stylyts.presentation.contracts.LoginContract
import kz.eztech.stylyts.presentation.contracts.RegistrationContract
import javax.inject.Inject

/**
 * Created by Ruslan Erdenoff on 18.12.2020.
 */
class LoginPresenter:LoginContract.Presenter {
    private var errorHelper: ErrorHelper
    private var loginUseCase: LoginUseCase
    private lateinit var view: LoginContract.View
    @Inject
    constructor(errorHelper: ErrorHelper,
                loginUseCase: LoginUseCase
    ){
        this.loginUseCase = loginUseCase
        this.errorHelper = errorHelper
    }
    override fun disposeRequests() {
        loginUseCase.clear()
    }

    override fun attach(view: LoginContract.View) {
        this.view = view
    }

    override fun loginUser(data: HashMap<String, Any>) {
        view.displayProgress()
        loginUseCase.initParams(data)
        loginUseCase.execute(object : DisposableSingleObserver<UserModel>(){
            override fun onSuccess(t: UserModel) {
                view.processViewAction {
                    hideProgress()
                    processLoginUser(t)
                }
            }

            override fun onError(e: Throwable) {
                view.processViewAction {
                    hideProgress()
                    displayMessage(errorHelper.processError(e))
                }
            }
        })
    }
}