package kz.eztech.stylyts.profile.presentation.profile.presenters

import io.reactivex.observers.DisposableSingleObserver
import kz.eztech.stylyts.global.data.exception.ErrorHelper
import kz.eztech.stylyts.global.domain.models.user.UserModel
import kz.eztech.stylyts.profile.domain.usecases.profile.GetProfileUseCase
import kz.eztech.stylyts.profile.presentation.profile.contracts.MyDataContract
import javax.inject.Inject

class MyDataPresenter @Inject constructor(
    private val errorHelper: ErrorHelper,
    private val getProfileUseCase: GetProfileUseCase,
) : MyDataContract.Presenter {

    private lateinit var view: MyDataContract.View

    override fun disposeRequests() {
        getProfileUseCase.clear()
    }

    override fun attach(view: MyDataContract.View) {
        this.view = view
    }

    override fun getProfile() {
        getProfileUseCase.initParams()
        getProfileUseCase.execute(object : DisposableSingleObserver<UserModel>() {
            override fun onSuccess(t: UserModel) {
                view.processProfile(userModel = t)
            }

            override fun onError(e: Throwable) {
                view.displayMessage(msg = errorHelper.processError(e))
            }
        })
    }
}