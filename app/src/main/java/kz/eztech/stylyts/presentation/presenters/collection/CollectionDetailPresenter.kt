package kz.eztech.stylyts.presentation.presenters.collection

import io.reactivex.observers.DisposableSingleObserver
import kz.eztech.stylyts.data.exception.ErrorHelper
import kz.eztech.stylyts.domain.models.common.ActionModel
import kz.eztech.stylyts.domain.models.outfits.OutfitModel
import kz.eztech.stylyts.domain.models.posts.PostModel
import kz.eztech.stylyts.domain.models.user.UserModel
import kz.eztech.stylyts.domain.usecases.outfits.DeleteOutfitUseCase
import kz.eztech.stylyts.domain.usecases.outfits.GetOutfitByIdUseCase
import kz.eztech.stylyts.domain.usecases.posts.DeletePostUseCase
import kz.eztech.stylyts.domain.usecases.posts.GetPostByIdUseCase
import kz.eztech.stylyts.domain.usecases.posts.LikePostUseCase
import kz.eztech.stylyts.domain.usecases.profile.GetUserByIdUseCase
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
    private val likePostUseCase: LikePostUseCase,
    private val getUserByIdUseCase: GetUserByIdUseCase
) : CollectionDetailContract.Presenter {

    private lateinit var view: CollectionDetailContract.View

    override fun disposeRequests() {
        getOutfitByIdUseCase.clear()
        getPostByIdUseCase.clear()
        deleteOutfitUseCase.clear()
        deletePostUseCase.clear()
        likePostUseCase.clear()
        getUserByIdUseCase.clear()
    }

    override fun attach(view: CollectionDetailContract.View) {
        this.view = view
    }

    override fun getOutfitById(outfitId: Int) {
        getOutfitByIdUseCase.initParams(outfitId)
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

    override fun getPostById(postId: Int) {
        getPostByIdUseCase.initParams(postId)
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

    override fun deleteOutfit(outfitId: Int) {
        view.displayProgress()

        deleteOutfitUseCase.initParams(outfitId)
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

    override fun onLikeClick(postId: Int) {
        likePostUseCase.initParams(postId)
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