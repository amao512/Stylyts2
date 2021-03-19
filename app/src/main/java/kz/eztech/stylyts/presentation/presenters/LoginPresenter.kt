package kz.eztech.stylyts.presentation.presenters

import io.reactivex.observers.DisposableSingleObserver
import kz.eztech.stylyts.data.exception.ErrorHelper
import kz.eztech.stylyts.domain.models.AuthModel
import kz.eztech.stylyts.domain.models.UserModel
import kz.eztech.stylyts.domain.usecases.LoginUseCase
import kz.eztech.stylyts.domain.usecases.main.profile.GetProfileUseCase
import kz.eztech.stylyts.presentation.base.processViewAction
import kz.eztech.stylyts.presentation.contracts.LoginContract
import javax.inject.Inject

/**
 * Created by Ruslan Erdenoff on 18.12.2020.
 */
class LoginPresenter @Inject constructor(
    private var errorHelper: ErrorHelper,
    private var loginUseCase: LoginUseCase,
    private var getProfileUseCase: GetProfileUseCase
) : LoginContract.Presenter {

    private lateinit var view: LoginContract.View

    override fun disposeRequests() {
        loginUseCase.clear()
    }

    override fun attach(view: LoginContract.View) {
        this.view = view
    }

    override fun loginUser(data: HashMap<String, Any>) {
        view.displayProgress()
        loginUseCase.initParams(data)
        loginUseCase.execute(object : DisposableSingleObserver<AuthModel>(){
            override fun onSuccess(t: AuthModel) {
                view.processViewAction {
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

    override fun getUserProfile(token: String) {
        getProfileUseCase.initParams(token)
        getProfileUseCase.execute(object : DisposableSingleObserver<UserModel>() {

            override fun onSuccess(t: UserModel) {
                view.processViewAction {
                    hideProgress()
                    processUserProfile(userModel = t)
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