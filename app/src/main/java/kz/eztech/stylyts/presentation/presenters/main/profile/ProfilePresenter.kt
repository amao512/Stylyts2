package kz.eztech.stylyts.presentation.presenters.main.profile

import io.reactivex.observers.DisposableSingleObserver
import kz.eztech.stylyts.data.exception.ErrorHelper
import kz.eztech.stylyts.domain.models.UserModel
import kz.eztech.stylyts.domain.usecases.LoginUseCase
import kz.eztech.stylyts.domain.usecases.main.profile.GetProfileUseCase
import kz.eztech.stylyts.presentation.base.processViewAction
import kz.eztech.stylyts.presentation.contracts.LoginContract
import kz.eztech.stylyts.presentation.contracts.main.profile.ProfileContract
import javax.inject.Inject

/**
 * Created by Ruslan Erdenoff on 25.12.2020.
 */
class ProfilePresenter: ProfileContract.Presenter {
	private var errorHelper: ErrorHelper
	private var getProfileUseCase: GetProfileUseCase
	private lateinit var view: ProfileContract.View
	@Inject
	constructor(errorHelper: ErrorHelper,
	            getProfileUseCase: GetProfileUseCase
	){
		this.getProfileUseCase = getProfileUseCase
		this.errorHelper = errorHelper
	}
	override fun disposeRequests() {
		getProfileUseCase.clear()
	}
	
	override fun attach(view: ProfileContract.View) {
		this.view = view
	}
	
	override fun getProfile(token: String) {
		view.displayProgress()
		getProfileUseCase.initParams(token)
		getProfileUseCase.execute(object : DisposableSingleObserver<UserModel>(){
			override fun onSuccess(t: UserModel) {
				view.processViewAction {
					hideProgress()
					processProfile(t)
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