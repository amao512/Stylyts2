package kz.eztech.stylyts.presentation.presenters.main

import io.reactivex.observers.DisposableSingleObserver
import kz.eztech.stylyts.data.exception.ErrorHelper
import kz.eztech.stylyts.domain.models.ResultsModel
import kz.eztech.stylyts.domain.models.posts.PostModel
import kz.eztech.stylyts.domain.usecases.posts.GetHomePagePostsUseCase
import kz.eztech.stylyts.domain.usecases.posts.GetPostsUseCase
import kz.eztech.stylyts.presentation.base.processViewAction
import kz.eztech.stylyts.presentation.contracts.main.MainContract
import javax.inject.Inject

/**
 * Created by Ruslan Erdenoff on 14.01.2021.
 */
class MainLinePresenter @Inject constructor(
	private val errorHelper: ErrorHelper,
	private val getPostsUseCase: GetPostsUseCase,
	private val getHomePagePostsUseCase: GetHomePagePostsUseCase
) : MainContract.Presenter {

	private lateinit var view: MainContract.View

	override fun disposeRequests() {
		getPostsUseCase.clear()
		getHomePagePostsUseCase.clear()
	}

	override fun attach(view: MainContract.View) {
		this.view = view
	}

	override fun getPosts(token: String) {
		view.displayProgress()

		getPostsUseCase.initParams(token)
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