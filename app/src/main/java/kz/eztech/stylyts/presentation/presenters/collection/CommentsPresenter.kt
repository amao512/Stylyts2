package kz.eztech.stylyts.presentation.presenters.collection

import io.reactivex.observers.DisposableSingleObserver
import kz.eztech.stylyts.data.api.models.comments.CommentCreateModel
import kz.eztech.stylyts.data.exception.ErrorHelper
import kz.eztech.stylyts.domain.models.common.ResultsModel
import kz.eztech.stylyts.domain.models.comments.CommentModel
import kz.eztech.stylyts.domain.models.common.PageFilterModel
import kz.eztech.stylyts.domain.models.outfits.OutfitModel
import kz.eztech.stylyts.domain.models.posts.PostModel
import kz.eztech.stylyts.domain.models.user.UserModel
import kz.eztech.stylyts.domain.usecases.comments.CreateCommentUseCase
import kz.eztech.stylyts.domain.usecases.comments.GetCommentsUseCase
import kz.eztech.stylyts.domain.usecases.outfits.GetOutfitByIdUseCase
import kz.eztech.stylyts.domain.usecases.posts.GetPostByIdUseCase
import kz.eztech.stylyts.domain.usecases.profile.GetProfileUseCase
import kz.eztech.stylyts.presentation.base.processViewAction
import kz.eztech.stylyts.presentation.contracts.collection.CommentsContract
import javax.inject.Inject

class CommentsPresenter @Inject constructor(
    private val errorHelper: ErrorHelper,
    private val getProfileUseCase: GetProfileUseCase,
    private val getPostByIdUseCase: GetPostByIdUseCase,
    private val getOutfitByIdUseCase: GetOutfitByIdUseCase,
    private val getCommentsUseCase: GetCommentsUseCase,
    private val createCommentUseCase: CreateCommentUseCase
) : CommentsContract.Presenter {

    private lateinit var view: CommentsContract.View

    override fun disposeRequests() {
        getProfileUseCase.clear()
        getCommentsUseCase.clear()
        createCommentUseCase.clear()
        getPostByIdUseCase.clear()
        getOutfitByIdUseCase.clear()
    }

    override fun attach(view: CommentsContract.View) {
        this.view = view
    }

    override fun getCollection(
        token: String,
        id: Int,
        mode: Int
    ) {
        when (mode) {
            0 -> getOutfit(token, id)
            1 -> getPost(token, id)
        }
    }

    override fun getPost(
        token: String,
        postId: Int
    ) {
        getPostByIdUseCase.initParams(token, postId)
        getPostByIdUseCase.execute(object : DisposableSingleObserver<PostModel>() {
            override fun onSuccess(t: PostModel) {
                view.processPost(postModel = t)
            }

            override fun onError(e: Throwable) {
                view.displayMessage(msg = errorHelper.processError(e))
            }
        })
    }

    override fun getOutfit(
        token: String,
        outfitId: Int
    ) {
        getOutfitByIdUseCase.initParams(token, outfitId)
        getOutfitByIdUseCase.execute(object : DisposableSingleObserver<OutfitModel>() {
            override fun onSuccess(t: OutfitModel) {
                view.processOutfit(outfitModel = t)
            }

            override fun onError(e: Throwable) {
                view.displayMessage(msg = errorHelper.processError(e))
            }
        })
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
        postId: Int,
        pageFilterModel: PageFilterModel
    ) {
        getCommentsUseCase.initParams(token, postId, pageFilterModel)
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