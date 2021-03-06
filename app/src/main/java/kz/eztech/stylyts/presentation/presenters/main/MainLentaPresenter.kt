package kz.eztech.stylyts.presentation.presenters.main

import io.reactivex.observers.DisposableSingleObserver
import kz.eztech.stylyts.data.exception.ErrorHelper
import kz.eztech.stylyts.domain.models.MainLentaModel
import kz.eztech.stylyts.domain.models.UserModel
import kz.eztech.stylyts.domain.usecases.main.MainLentaUseCase
import kz.eztech.stylyts.domain.usecases.main.profile.GetProfileUseCase
import kz.eztech.stylyts.presentation.base.processViewAction
import kz.eztech.stylyts.presentation.contracts.main.MainContract
import kz.eztech.stylyts.presentation.contracts.main.profile.ProfileContract
import javax.inject.Inject

/**
 * Created by Ruslan Erdenoff on 14.01.2021.
 */
class MainLentaPresenter:MainContract.Presenter {
	private var errorHelper: ErrorHelper
	private var getCollectionsUseCase: MainLentaUseCase
	private lateinit var view: MainContract.View
	@Inject
	constructor(errorHelper: ErrorHelper,
	            getCollectionsUseCase: MainLentaUseCase
	){
		this.getCollectionsUseCase = getCollectionsUseCase
		this.errorHelper = errorHelper
	}
	override fun getCollections(token: String) {
		view.displayProgress()
		getCollectionsUseCase.initParams(token)
		getCollectionsUseCase.execute(object : DisposableSingleObserver<MainLentaModel>(){
			override fun onSuccess(t: MainLentaModel) {
				view.processViewAction {
					hideProgress()
					processCollections(t)
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
		getCollectionsUseCase.clear()
	}
	
	override fun attach(view: MainContract.View) {
		this.view = view
	}
}