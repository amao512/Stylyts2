package kz.eztech.stylyts.presentation.presenters.collection

import io.reactivex.observers.DisposableSingleObserver
import kz.eztech.stylyts.data.exception.ErrorHelper
import kz.eztech.stylyts.domain.models.common.ResultsModel
import kz.eztech.stylyts.domain.models.filter.FilterModel
import kz.eztech.stylyts.domain.models.outfits.OutfitModel
import kz.eztech.stylyts.domain.models.posts.PostModel
import kz.eztech.stylyts.domain.usecases.outfits.GetOutfitsUseCase
import kz.eztech.stylyts.domain.usecases.posts.GetPostsUseCase
import kz.eztech.stylyts.presentation.base.processViewAction
import kz.eztech.stylyts.presentation.contracts.collection.CollectionItemContract
import javax.inject.Inject

/**
 * Created by Ruslan Erdenoff on 01.02.2021.
 */
class CollectionsItemPresenter @Inject constructor(
	private val errorHelper: ErrorHelper,
	private val getOutfitsUseCase: GetOutfitsUseCase,
	private val getPostsUseCase: GetPostsUseCase
) : CollectionItemContract.Presenter {

	private lateinit var view: CollectionItemContract.View

	override fun disposeRequests() {
		getOutfitsUseCase.clear()
		getPostsUseCase.clear()
	}

	override fun attach(view: CollectionItemContract.View) {
		this.view = view
	}

	override fun getOutfits(
		token: String,
		filterModel: FilterModel
	) {
		view.displayProgress()

		getOutfitsUseCase.initParams(token, filterModel)
		getOutfitsUseCase.execute(object : DisposableSingleObserver<ResultsModel<OutfitModel>>(){
			override fun onSuccess(t: ResultsModel<OutfitModel>) {
				view.processViewAction {
					hideProgress()
					processOutfits(t)
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

	override fun getPosts(
		token: String,
		filterModel: FilterModel
	) {
		view.displayProgress()

		getPostsUseCase.initParams(token, filterModel)
		getPostsUseCase.execute(object : DisposableSingleObserver<ResultsModel<PostModel>>() {
			override fun onSuccess(t: ResultsModel<PostModel>) {
				view.processViewAction {
					processPostResults(resultsModel = t)
					hideProgress()
				}
			}

			override fun onError(e: Throwable) {
				view.processViewAction {
					hideProgress()
					displayMessage(msg = errorHelper.processError(e))
				}
			}
		})
	}
}