package kz.eztech.stylyts.presentation.presenters.collection

import io.reactivex.observers.DisposableSingleObserver
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.launch
import kz.eztech.stylyts.data.api.models.comments.CommentCreateModel
import kz.eztech.stylyts.data.exception.ErrorHelper
import kz.eztech.stylyts.domain.models.comments.CommentModel
import kz.eztech.stylyts.domain.models.common.ResultsModel
import kz.eztech.stylyts.domain.models.user.UserModel
import kz.eztech.stylyts.domain.usecases.comments.CreateCommentUseCase
import kz.eztech.stylyts.domain.usecases.comments.GetCommentsUseCase
import kz.eztech.stylyts.domain.usecases.outfits.GetOutfitByIdUseCase
import kz.eztech.stylyts.domain.usecases.posts.GetPostByIdUseCase
import kz.eztech.stylyts.domain.usecases.profile.GetProfileUseCase
import kz.eztech.stylyts.presentation.contracts.collection.CommentsContract
import kz.eztech.stylyts.presentation.utils.Paginator
import javax.inject.Inject

class CommentsPresenter @Inject constructor(
    private val errorHelper: ErrorHelper,
    private val paginator: Paginator.Store<CommentModel>,
    private val getProfileUseCase: GetProfileUseCase,
    private val getPostByIdUseCase: GetPostByIdUseCase,
    private val getOutfitByIdUseCase: GetOutfitByIdUseCase,
    private val getCommentsUseCase: GetCommentsUseCase,
    private val createCommentUseCase: CreateCommentUseCase
) : CommentsContract.Presenter, CoroutineScope by CoroutineScope(Dispatchers.Main) {

    private lateinit var view: CommentsContract.View

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
        getProfileUseCase.clear()
        getCommentsUseCase.clear()
        createCommentUseCase.clear()
        getPostByIdUseCase.clear()
        getOutfitByIdUseCase.clear()
        cancel()
    }

    override fun attach(view: CommentsContract.View) {
        this.view = view
    }

    override fun getProfile() {
        getProfileUseCase.initParams()
        getProfileUseCase.execute(object : DisposableSingleObserver<UserModel>() {
            override fun onSuccess(t: UserModel) {
                view.processProfile(userModel = t)
            }

            override fun onError(e: Throwable) {
                view.displayMessage(msg = errorHelper.processError(e))
            }
        })
    }

    override fun getComments() {
        paginator.proceed(Paginator.Action.Refresh)
    }

    override fun loadMoreComments() {
        paginator.proceed(Paginator.Action.LoadMore)
    }

    override fun loadPage(page: Int) {
        getCommentsUseCase.initParams(
            postId = view.getPostId(),
            page = page
        )
        getCommentsUseCase.execute(object : DisposableSingleObserver<ResultsModel<CommentModel>>() {
            override fun onSuccess(t: ResultsModel<CommentModel>) {
                paginator.proceed(
                    Paginator.Action.NewPage(pageNumber = t.page, items = t.results)
                )
            }

            override fun onError(e: Throwable) {
                paginator.proceed(Paginator.Action.PageError(e))
            }
        })
    }

    override fun createComment(commentCreateModel: CommentCreateModel) {
        createCommentUseCase.initParams(commentCreateModel)
        createCommentUseCase.execute(object : DisposableSingleObserver<CommentModel>() {
            override fun onSuccess(t: CommentModel) {
                view.processCreatingComment(commentModel = t)
            }

            override fun onError(e: Throwable) {
                view.displayMessage(msg = errorHelper.processError(e))
            }
        })
    }
}