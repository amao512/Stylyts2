package kz.eztech.stylyts.presentation.presenters.collection

import io.reactivex.observers.DisposableSingleObserver
import kz.eztech.stylyts.data.exception.ErrorHelper
import kz.eztech.stylyts.domain.models.outfits.OutfitModel
import kz.eztech.stylyts.domain.models.posts.PostModel
import kz.eztech.stylyts.domain.models.user.UserModel
import kz.eztech.stylyts.domain.usecases.outfits.GetOutfitByIdUseCase
import kz.eztech.stylyts.domain.usecases.posts.GetPostByIdUseCase
import kz.eztech.stylyts.domain.usecases.profile.GetUserByIdUseCase
import kz.eztech.stylyts.presentation.base.processViewAction
import kz.eztech.stylyts.presentation.contracts.collection.CollectionDetailContract
import javax.inject.Inject

class CollectionDetailPresenter @Inject constructor(
    private val errorHelper: ErrorHelper,
    private val getOutfitByIdUseCase: GetOutfitByIdUseCase,
    private val getUserByIdUseCase: GetUserByIdUseCase,
    private val getPostByIdUseCase: GetPostByIdUseCase
) : CollectionDetailContract.Presenter {

    private lateinit var view: CollectionDetailContract.View

    override fun disposeRequests() {
        view.disposeRequests()
        getOutfitByIdUseCase.clear()
        getPostByIdUseCase.clear()
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

    override fun getOwner(token: String, userId: String) {
        getUserByIdUseCase.initParams(token, userId)
        getUserByIdUseCase.execute(object : DisposableSingleObserver<UserModel>() {
            override fun onSuccess(t: UserModel) {
                view.processOwner(userModel = t)
            }

            override fun onError(e: Throwable) {
               view.displayMessage(msg = errorHelper.processError(e))
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
}