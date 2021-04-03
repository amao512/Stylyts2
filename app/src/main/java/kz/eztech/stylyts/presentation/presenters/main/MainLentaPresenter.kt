package kz.eztech.stylyts.presentation.presenters.main

import io.reactivex.observers.DisposableSingleObserver
import kz.eztech.stylyts.domain.models.PublicationModel
import kz.eztech.stylyts.data.exception.ErrorHelper
import kz.eztech.stylyts.domain.models.MainLentaModel
import kz.eztech.stylyts.domain.usecases.main.MainLentaUseCase
import kz.eztech.stylyts.presentation.base.processViewAction
import kz.eztech.stylyts.presentation.contracts.main.MainContract
import kz.eztech.stylyts.domain.usecases.profile.GetMyPublicationsUseCase
import kz.eztech.stylyts.domain.models.ResultsModel
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
		getMyPublicationsUseCase.execute(object : DisposableSingleObserver<ResultsModel<PublicationModel>>() {
			override fun onSuccess(t: ResultsModel<PublicationModel>) {
				view.processViewAction {
					hideProgress()
					processMyPublications(resultsModel = t)
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