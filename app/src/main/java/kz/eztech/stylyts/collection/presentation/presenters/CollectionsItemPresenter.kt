package kz.eztech.stylyts.collection.presentation.presenters

import io.reactivex.observers.DisposableSingleObserver
import kz.eztech.stylyts.common.data.exception.ErrorHelper
import kz.eztech.stylyts.common.domain.models.MainLentaModel
import kz.eztech.stylyts.main.domain.usecases.MainLentaUseCase
import kz.eztech.stylyts.common.presentation.base.processViewAction
import kz.eztech.stylyts.collection.presentation.contracts.CollectionItemContract
import javax.inject.Inject

/**
 * Created by Ruslan Erdenoff on 01.02.2021.
 */
class CollectionsItemPresenter @Inject constructor(
	private var errorHelper: ErrorHelper,
	private var getCollectionsUseCase: MainLentaUseCase
) : CollectionItemContract.Presenter {
	private lateinit var view: CollectionItemContract.View

	override fun getCollections(
		token: String,
		map: Map<String, Any>?
	) {
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