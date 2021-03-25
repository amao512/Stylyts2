package kz.eztech.stylyts.main.presentation.presenters

import io.reactivex.observers.DisposableSingleObserver
import kz.eztech.stylyts.collection_constructor.domain.models.PublicationModel
import kz.eztech.stylyts.common.data.exception.ErrorHelper
import kz.eztech.stylyts.common.domain.models.MainLentaModel
import kz.eztech.stylyts.main.domain.usecases.MainLentaUseCase
import kz.eztech.stylyts.common.presentation.base.processViewAction
import kz.eztech.stylyts.common.presentation.contracts.main.MainContract
import kz.eztech.stylyts.profile.domain.usecases.GetMyPublicationsUseCase
import kz.eztech.stylyts.search.domain.models.SearchModel
import javax.inject.Inject

/**
 * Created by Ruslan Erdenoff on 14.01.2021.
 */
class MainLentaPresenter @Inject constructor(
	private var errorHelper: ErrorHelper,
	private var getCollectionsUseCase: MainLentaUseCase,
	private val getMyPublicationsUseCase: GetMyPublicationsUseCase
) : MainContract.Presenter {

	private lateinit var view: MainContract.View

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

	override fun getMyPublications(token: String) {
		view.displayProgress()

		getMyPublicationsUseCase.initParams(token)
		getMyPublicationsUseCase.execute(object : DisposableSingleObserver<SearchModel<PublicationModel>>() {
			override fun onSuccess(t: SearchModel<PublicationModel>) {
				view.processViewAction {
					hideProgress()
					processMyPublications(searchModel = t)
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