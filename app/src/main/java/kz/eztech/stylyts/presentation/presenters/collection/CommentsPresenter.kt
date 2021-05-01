package kz.eztech.stylyts.presentation.presenters.collection

import io.reactivex.observers.DisposableSingleObserver
import kz.eztech.stylyts.data.api.models.comments.CommentCreateModel
import kz.eztech.stylyts.data.exception.ErrorHelper
import kz.eztech.stylyts.domain.models.ResultsModel
import kz.eztech.stylyts.domain.models.comments.CommentModel
import kz.eztech.stylyts.domain.models.user.UserModel
import kz.eztech.stylyts.domain.usecases.comments.CreateCommentUseCase
import kz.eztech.stylyts.domain.usecases.comments.GetCommentsUseCase
import kz.eztech.stylyts.domain.usecases.profile.GetProfileUseCase
import kz.eztech.stylyts.presentation.base.processViewAction
import kz.eztech.stylyts.presentation.contracts.collection.CommentsContract
import javax.inject.Inject

class CommentsPresenter @Inject constructor(
    private val errorHelper: ErrorHelper,
    private val getProfileUseCase: GetProfileUseCase,
    private val getCommentsUseCase: GetCommentsUseCase,
    private val createCommentUseCase: CreateCommentUseCase
) : CommentsContract.Presenter {

    private lateinit var view: CommentsContract.View

    override fun disposeRequests() {
        getProfileUseCase.clear()
        getCommentsUseCase.clear()
        createCommentUseCase.clear()
    }

    override fun attach(view: CommentsContract.View) {
        this.view = view
    }

    override fun getProfile(token: String) {
        getProfileUseCase.initParams(token)
        getProfileUseCase.execute(object : DisposableSingleObserver<UserModel>() {
            override fun onSuccess(t: UserModel) {
                view.processProfile(userModel = t)
            }

            override fun onError(e: Throwable) {
                view.displayMessage(msg = errorHelper.processError(e))
            }
        })
    }

    override fun getComments(
        token: String,
        postId: Int
    ) {
        view.displayProgress()

        getCommentsUseCase.initParams(token, postId)
        getCommentsUseCase.execute(object : DisposableSingleObserver<ResultsModel<CommentModel>>() {
            override fun onSuccess(t: ResultsModel<CommentModel>) {
                view.processViewAction {
                    processComments(results = t)
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

    override fun createComment(
        token: String,
        text: String,
        postId: Int
    ) {
        val commentCreateModel = CommentCreateModel(text = text, postId = postId)

        createCommentUseCase.initParams(token, commentCreateModel)
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