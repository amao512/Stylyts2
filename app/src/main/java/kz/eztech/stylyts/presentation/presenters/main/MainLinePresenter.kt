package kz.eztech.stylyts.presentation.presenters.main

import io.reactivex.observers.DisposableSingleObserver
import kz.eztech.stylyts.data.exception.ErrorHelper
import kz.eztech.stylyts.domain.models.common.ActionModel
import kz.eztech.stylyts.domain.models.common.ResultsModel
import kz.eztech.stylyts.domain.models.posts.PostModel
import kz.eztech.stylyts.domain.usecases.posts.DeletePostUseCase
import kz.eztech.stylyts.domain.usecases.posts.GetHomePagePostsUseCase
import kz.eztech.stylyts.domain.usecases.posts.LikePostUseCase
import kz.eztech.stylyts.presentation.base.processViewAction
import kz.eztech.stylyts.presentation.contracts.main.MainContract
import kz.eztech.stylyts.presentation.enums.LikeEnum
import javax.inject.Inject

/**
 * Created by Ruslan Erdenoff on 14.01.2021.
 */
class MainLinePresenter @Inject constructor(
	private val errorHelper: ErrorHelper,
	private val getHomePagePostsUseCase: GetHomePagePostsUseCase,
	private val deletePostUseCase: DeletePostUseCase,
	private val likePostUseCase: LikePostUseCase
) : MainContract.Presenter {

	private lateinit var view: MainContract.View

	override fun disposeRequests() {
		getHomePagePostsUseCase.clear()
		deletePostUseCase.clear()
		likePostUseCase.clear()
	}

	override fun attach(view: MainContract.View) {
		this.view = view
	}

	override fun getPosts(
		token: String,
		page: Int
	) {
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

	override fun likePost(
		token: String,
		postId: Int
	) {
		likePostUseCase.initParams(token, postId)
		likePostUseCase.execute(object : DisposableSingleObserver<ActionModel>() {
			override fun onSuccess(t: ActionModel) {
				when (t.action) {
					LikeEnum.LIKE.title -> view.processLike(isLiked = true, postId)
					LikeEnum.UNLIKE.title -> view.processLike(isLiked = false, postId)
				}
			}

			override fun onError(e: Throwable) {
				view.displayMessage(msg = errorHelper.processError(e))
			}
		})
	}
}