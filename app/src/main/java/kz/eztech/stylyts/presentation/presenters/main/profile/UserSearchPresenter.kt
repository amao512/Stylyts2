package kz.eztech.stylyts.presentation.presenters.main.profile

import io.reactivex.observers.DisposableSingleObserver
import kz.eztech.stylyts.data.exception.ErrorHelper
import kz.eztech.stylyts.domain.models.UserSearchModel
import kz.eztech.stylyts.domain.usecases.LoginUseCase
import kz.eztech.stylyts.domain.usecases.main.profile.UserSearchUseCase
import kz.eztech.stylyts.presentation.base.processViewAction
import kz.eztech.stylyts.presentation.contracts.LoginContract
import kz.eztech.stylyts.presentation.contracts.main.profile.UserSearchContract
import javax.inject.Inject

/**
 * Created by Ruslan Erdenoff on 22.02.2021.
 */
class UserSearchPresenter:UserSearchContract.Presenter {
	private var errorHelper: ErrorHelper
	private var userSearchUseCase: UserSearchUseCase
	private lateinit var view: UserSearchContract.View
	@Inject
	constructor(errorHelper: ErrorHelper,
	            userSearchUseCase: UserSearchUseCase
	){
		this.userSearchUseCase = userSearchUseCase
		this.errorHelper = errorHelper
	}
	override fun getUserByUsername(token: String, username: String) {
		view.displayProgress()
		userSearchUseCase.initParams(token,username)
		userSearchUseCase.execute(object : DisposableSingleObserver<List<UserSearchModel>>(){
			override fun onSuccess(t: List<UserSearchModel>) {
				view.processViewAction{
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
		})
	}
	
	override fun disposeRequests() {
		userSearchUseCase.clear()
	}
	
	override fun attach(view: UserSearchContract.View) {
		this.view = view
	}
}