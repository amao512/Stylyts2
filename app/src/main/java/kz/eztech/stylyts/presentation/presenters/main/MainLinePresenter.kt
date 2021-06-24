package kz.eztech.stylyts.presentation.presenters.main

import io.reactivex.observers.DisposableSingleObserver
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.launch
import kz.eztech.stylyts.data.exception.ErrorHelper
import kz.eztech.stylyts.domain.models.common.ActionModel
import kz.eztech.stylyts.domain.models.common.ResultsModel
import kz.eztech.stylyts.domain.models.posts.PostModel
import kz.eztech.stylyts.domain.models.user.UserModel
import kz.eztech.stylyts.domain.usecases.posts.DeletePostUseCase
import kz.eztech.stylyts.domain.usecases.posts.GetHomePagePostsUseCase
import kz.eztech.stylyts.domain.usecases.posts.LikePostUseCase
import kz.eztech.stylyts.domain.usecases.profile.GetUserByIdUseCase
import kz.eztech.stylyts.presentation.base.processViewAction
import kz.eztech.stylyts.presentation.contracts.main.MainContract
import kz.eztech.stylyts.presentation.enums.LikeEnum
import kz.eztech.stylyts.presentation.utils.Paginator
import javax.inject.Inject

/**
 * Created by Ruslan Erdenoff on 14.01.2021.
 */
class MainLinePresenter @Inject constructor(
	private val errorHelper: ErrorHelper,
	private val paginator: Paginator.Store<PostModel>,
	private val getHomePagePostsUseCase: GetHomePagePostsUseCase,
	private val deletePostUseCase: DeletePostUseCase,
	private val likePostUseCase: LikePostUseCase,
	private val getUserByIdUseCase: GetUserByIdUseCase
) : MainContract.Presenter, CoroutineScope by CoroutineScope(Dispatchers.Main) {

	private lateinit var view: MainContract.View

	init {
	    launch {
	    	paginator.render = { view.renderPaginatorState(it) }
			paginator.sideEffects.consumeEach { effect ->
				when (effect) {
					is Paginator.SideEffect.LoadPage -> loadPage(effect.currentPage)
					is Paginator.SideEffect.ErrorEvent -> {}
				}
			}
		}
	}

	override fun disposeRequests() {
		getHomePagePostsUseCase.clear()
		deletePostUseCase.clear()
		likePostUseCase.clear()
		getUserByIdUseCase.clear()
	}

	override fun attach(view: MainContract.View) {
		this.view = view
	}

	override fun loadPage(page: Int) {
		getHomePagePostsUseCase.initParams(view.getToken(), page)
		getHomePagePostsUseCase.execute(object : DisposableSingleObserver<ResultsModel<PostModel>>() {
			override fun onSuccess(t: ResultsModel<PostModel>) {
				paginator.proceed(Paginator.Action.NewPage(
					pageNumber = t.page,
					items = t.results
				))
			}

			override fun onError(e: Throwable) {
				paginator.proceed(Paginator.Action.PageError(error = e))
			}
		})
	}

	override fun getPosts() {
		paginator.proceed(Paginator.Action.Refresh)
	}

	override fun loadMorePost() {
		paginator.proceed(Paginator.Action.LoadMore)
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

	override fun getUserForNavigate(token: String, userId: Int) {
		getUserByIdUseCase.initParams(token, userId)
		getUserByIdUseCase.execute(object : DisposableSingleObserver<UserModel>() {
			override fun onSuccess(t: UserModel) {
				view.navigateToUserProfile(userModel = t)
			}

			override fun onError(e: Throwable) {
				view.displayMessage(msg = errorHelper.processError(e))
			}
		})
	}
}