package kz.eztech.stylyts.presentation.presenters.main

import io.reactivex.observers.DisposableSingleObserver
import kz.eztech.stylyts.data.exception.ErrorHelper
import kz.eztech.stylyts.domain.models.ResultsModel
import kz.eztech.stylyts.domain.models.posts.PostModel
import kz.eztech.stylyts.domain.usecases.posts.DeletePostUseCase
import kz.eztech.stylyts.domain.usecases.posts.GetHomePagePostsUseCase
import kz.eztech.stylyts.presentation.base.processViewAction
import kz.eztech.stylyts.presentation.contracts.main.MainContract
import javax.inject.Inject

/**
 * Created by Ruslan Erdenoff on 14.01.2021.
 */
class MainLinePresenter @Inject constructor(
	private val errorHelper: ErrorHelper,
	private val getHomePagePostsUseCase: GetHomePagePostsUseCase,
	private val deletePostUseCase: DeletePostUseCase
) : MainContract.Presenter {

	private lateinit var view: MainContract.View

	override fun disposeRequests() {
		getHomePagePostsUseCase.clear()
		deletePostUseCase.clear()
	}

	override fun attach(view: MainContract.View) {
		this.view = view
	}

	override fun getPosts(
		token: String,
		page: Int
	) {
		view.displayProgress()

		getHomePagePostsUseCase.initParams(token, page)
		getHomePagePostsUseCase.execute(object : DisposableSingleObserver<ResultsModel<PostModel>>() {
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

	override fun deletePost(
		token: String,
		postId: Int
	) {
		view.displayProgress()

		deletePostUseCase.initParams(token, postId)
		deletePostUseCase.execute(object : DisposableSingleObserver<Any>() {
			override fun onSuccess(t: Any) {
				view.processViewAction {
					hideProgress()
					processSuccessDeleting()
				}
			}

			override fun onError(e: Throwable) {
				view.processViewAction {
					hideProgress()
					processSuccessDeleting()
				}
			}
		})
	}
}