package kz.eztech.stylyts.presentation.presenters.main

import io.reactivex.observers.DisposableSingleObserver
import kz.eztech.stylyts.data.exception.ErrorHelper
import kz.eztech.stylyts.domain.models.MainLentaModel
import kz.eztech.stylyts.domain.usecases.main.MainLentaUseCase
import kz.eztech.stylyts.common.presentation.base.processViewAction
import kz.eztech.stylyts.presentation.contracts.main.collections.CollectionItemContract
import javax.inject.Inject

/**
 * Created by Ruslan Erdenoff on 01.02.2021.
 */
class CollectionsItemPresenter:CollectionItemContract.Presenter {
	private var errorHelper: ErrorHelper
	private var getCollectionsUseCase: MainLentaUseCase
	private lateinit var view: CollectionItemContract.View
	@Inject
	constructor(errorHelper: ErrorHelper,
	            getCollectionsUseCase: MainLentaUseCase
	){
		this.getCollectionsUseCase = getCollectionsUseCase
		this.errorHelper = errorHelper
	}
	
	override fun getCollections(token: String, map: Map<String, Any>?) {
		view.displayProgress()
		getCollectionsUseCase.initParams(token,map)
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
	
	override fun attach(view: CollectionItemContract.View) {
		this.view = view
	}
}