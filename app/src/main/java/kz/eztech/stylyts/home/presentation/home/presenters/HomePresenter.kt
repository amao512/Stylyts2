package kz.eztech.stylyts.home.presentation.home.presenters

import io.reactivex.observers.DisposableSingleObserver
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.launch
import kz.eztech.stylyts.global.data.exception.ErrorHelper
import kz.eztech.stylyts.global.domain.models.common.ActionModel
import kz.eztech.stylyts.global.domain.models.common.ResultsModel
import kz.eztech.stylyts.global.domain.models.posts.PostModel
import kz.eztech.stylyts.global.domain.models.user.UserModel
import kz.eztech.stylyts.global.domain.usecases.posts.DeletePostUseCase
import kz.eztech.stylyts.home.domain.usecases.GetHomePagePostsUseCase
import kz.eztech.stylyts.global.domain.usecases.posts.LikePostUseCase
import kz.eztech.stylyts.profile.domain.usecases.profile.GetUserByIdUseCase
import kz.eztech.stylyts.home.presentation.home.contracts.HomeContract
import kz.eztech.stylyts.global.presentation.base.processViewAction
import kz.eztech.stylyts.global.presentation.common.enums.LikeEnum
import kz.eztech.stylyts.utils.Paginator
import javax.inject.Inject

/**
 * Created by Ruslan Erdenoff on 14.01.2021.
 */
class HomePresenter @Inject constructor(
	private val errorHelper: ErrorHelper,
	private val paginator: Paginator.Store<PostModel>,
	private val getHomePagePostsUseCase: GetHomePagePostsUseCase,
	private val deletePostUseCase: DeletePostUseCase,
	private val likePostUseCase: LikePostUseCase,
	private val getUserByIdUseCase: GetUserByIdUseCase
) : HomeContract.Presenter, CoroutineScope by CoroutineScope(Dispatchers.Main) {

	private lateinit var view: HomeContract.View

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
		cancel()
	}

	override fun attach(view: HomeContract.View) {
		this.view = view
	}

	override fun loadPage(page: Int) {
		getHomePagePostsUseCase.initParams(page)
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

	override fun deletePost(postId: Int) {
		view.displayProgress()

		deletePostUseCase.initParams(postId)
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

	override fun likePost(postId: Int) {
		likePostUseCase.initParams(postId)
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

	override fun getUserForNavigate(userId: Int) {
		getUserByIdUseCase.initParams(userId)
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