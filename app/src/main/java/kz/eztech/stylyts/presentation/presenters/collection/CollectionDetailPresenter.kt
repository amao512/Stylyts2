package kz.eztech.stylyts.presentation.presenters.collection

import io.reactivex.observers.DisposableSingleObserver
import kz.eztech.stylyts.data.exception.ErrorHelper
import kz.eztech.stylyts.domain.models.ActionModel
import kz.eztech.stylyts.domain.models.outfits.OutfitModel
import kz.eztech.stylyts.domain.models.posts.PostModel
import kz.eztech.stylyts.domain.usecases.outfits.DeleteOutfitUseCase
import kz.eztech.stylyts.domain.usecases.outfits.GetOutfitByIdUseCase
import kz.eztech.stylyts.domain.usecases.posts.DeletePostUseCase
import kz.eztech.stylyts.domain.usecases.posts.GetPostByIdUseCase
import kz.eztech.stylyts.domain.usecases.posts.LikePostUseCase
import kz.eztech.stylyts.presentation.base.processViewAction
import kz.eztech.stylyts.presentation.contracts.collection.CollectionDetailContract
import kz.eztech.stylyts.presentation.enums.LikeEnum
import javax.inject.Inject

class CollectionDetailPresenter @Inject constructor(
    private val errorHelper: ErrorHelper,
    private val getOutfitByIdUseCase: GetOutfitByIdUseCase,
    private val getPostByIdUseCase: GetPostByIdUseCase,
    private val deleteOutfitUseCase: DeleteOutfitUseCase,
    private val deletePostUseCase: DeletePostUseCase,
    private val likePostUseCase: LikePostUseCase
) : CollectionDetailContract.Presenter {

    private lateinit var view: CollectionDetailContract.View

    override fun disposeRequests() {
        view.disposeRequests()
        getOutfitByIdUseCase.clear()
        getPostByIdUseCase.clear()
        deleteOutfitUseCase.clear()
        deletePostUseCase.clear()
        likePostUseCase.clear()
    }

    override fun attach(view: CollectionDetailContract.View) {
        this.view = view
    }

    override fun getOutfitById(token: String, outfitId: String) {
        view.displayProgress()

        getOutfitByIdUseCase.initParams(token, outfitId)
        getOutfitByIdUseCase.execute(object : DisposableSingleObserver<OutfitModel>() {
            override fun onSuccess(t: OutfitModel) {
                view.processViewAction {
                    processOutfit(outfitModel = t)
                    hideProgress()
                }
            }

            override fun onError(e: Throwable) {
                view.processViewAction {
                    displayMessage(msg = errorHelper.processError(e))
                    hideProgress()
                }
            }
        })
    }

    override fun getPostById(token: String, postId: Int) {
        view.displayProgress()

        getPostByIdUseCase.initParams(token, postId)
        getPostByIdUseCase.execute(object : DisposableSingleObserver<PostModel>() {
            override fun onSuccess(t: PostModel) {
                view.processViewAction {
                    processPost(postModel = t)
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

    override fun deleteOutfit(
        token: String,
        outfitId: Int
    ) {
        view.displayProgress()

        deleteOutfitUseCase.initParams(token, outfitId)
        deleteOutfitUseCase.execute(object : DisposableSingleObserver<Any>() {
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

    override fun onLikeClick(
        token: String,
        postId: Int
    ) {
        likePostUseCase.initParams(token, postId)
        likePostUseCase.execute(object : DisposableSingleObserver<ActionModel>() {
            override fun onSuccess(t: ActionModel) {
                when (t.action) {
                    LikeEnum.LIKE.title -> view.processLike(isLiked = true)
                    LikeEnum.UNLIKE.title -> view.processLike(isLiked = false)
                }
            }

            override fun onError(e: Throwable) {
                view.displayMessage(msg = errorHelper.processError(e))
            }
        })
    }
}