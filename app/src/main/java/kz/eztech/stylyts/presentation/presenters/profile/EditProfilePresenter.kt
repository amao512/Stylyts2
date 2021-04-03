package kz.eztech.stylyts.presentation.presenters.profile

import io.reactivex.observers.DisposableSingleObserver
import kz.eztech.stylyts.data.exception.ErrorHelper
import kz.eztech.stylyts.domain.models.UserModel
import kz.eztech.stylyts.domain.usecases.profile.EditProfileUseCase
import kz.eztech.stylyts.presentation.base.processViewAction
import kz.eztech.stylyts.presentation.contracts.profile.EditProfileContract
import javax.inject.Inject

/**
 * Created by Asylzhan Seytbek on 13.03.2021.
 */
class EditProfilePresenter @Inject constructor(
    private var errorHelper: ErrorHelper,
    private val editProfileUseCase: EditProfileUseCase
) : EditProfileContract.Presenter {

    private lateinit var view: EditProfileContract.View

    override fun attach(view: EditProfileContract.View) {
        this.view = view
    }

    override fun editProfile(
        token: String,
        data: HashMap<String, Any?>
    ) {
        view.displayProgress()

        editProfileUseCase.initParams(token, data)
        editProfileUseCase.execute(object : DisposableSingleObserver<UserModel>() {
            override fun onSuccess(t: UserModel) {
                view.processViewAction {
                    hideProgress()
                    successEditing(userModel = t)
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

    override fun disposeRequests() {
        editProfileUseCase.clear()
    }
}