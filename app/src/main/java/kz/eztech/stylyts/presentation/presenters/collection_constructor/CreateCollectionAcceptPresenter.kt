package kz.eztech.stylyts.presentation.presenters.collection_constructor

import io.reactivex.observers.DisposableSingleObserver
import kz.eztech.stylyts.data.exception.ErrorHelper
import kz.eztech.stylyts.domain.models.outfits.OutfitCreateModel
import kz.eztech.stylyts.domain.models.outfits.OutfitModel
import kz.eztech.stylyts.domain.models.posts.PostCreateModel
import kz.eztech.stylyts.domain.models.posts.PostModel
import kz.eztech.stylyts.domain.usecases.outfits.CreateOutfitUseCase
import kz.eztech.stylyts.domain.usecases.outfits.UpdateOutfitUseCase
import kz.eztech.stylyts.domain.usecases.posts.CreatePostUseCase
import kz.eztech.stylyts.domain.usecases.posts.UpdatePostUseCase
import kz.eztech.stylyts.presentation.base.processViewAction
import kz.eztech.stylyts.presentation.contracts.collection_constructor.CreateCollectionAcceptContract
import java.io.File
import javax.inject.Inject

class CreateCollectionAcceptPresenter @Inject constructor(
    private val errorHelper: ErrorHelper,
    private val createPostUseCase: CreatePostUseCase,
    private val updatePostUseCase: UpdatePostUseCase,
    private val createOutfitUseCase: CreateOutfitUseCase,
    private val updateOutfitUseCase: UpdateOutfitUseCase,
) : CreateCollectionAcceptContract.Presenter {

    private lateinit var view: CreateCollectionAcceptContract.View

    override fun disposeRequests() {
        view.disposeRequests()
        createPostUseCase.clear()
        updatePostUseCase.clear()
        createOutfitUseCase.clear()
        updateOutfitUseCase.clear()
    }

    override fun attach(view: CreateCollectionAcceptContract.View) {
        this.view = view
    }

    override fun createPost(
        token: String,
        postCreateModel: PostCreateModel
    ) {
        view.displayProgress()

        createPostUseCase.initParams(token, postCreateModel)
        createPostUseCase.execute(object : DisposableSingleObserver<PostModel>() {
            override fun onSuccess(t: PostModel) {
                view.processViewAction {
                    hideProgress()
                    processSuccessSaving()
                }
            }

            override fun onError(e: Throwable) {
                view.processViewAction {
                    hideProgress()
                    processSuccessSaving()
                }
            }
        })
    }

    override fun updatePost(
        token: String,
        postId: Int,
        postCreateModel: PostCreateModel
    ) {
        view.displayProgress()

        updatePostUseCase.initParams(token, postId, postCreateModel)
        updatePostUseCase.execute(object : DisposableSingleObserver<PostModel>() {
            override fun onSuccess(t: PostModel) {
                view.processViewAction {
                    hideProgress()
                    processSuccessSaving()
                }
            }

            override fun onError(e: Throwable) {
                view.processViewAction {
                    hideProgress()
                    processSuccessSaving()
                }
            }
        })
    }

    override fun createOutfit(token: String, model: OutfitCreateModel, data: File) {
        view.displayProgress()

        createOutfitUseCase.initParam(
            token = token,
            file = data,
            outfitCreateModel = model
        )
        createOutfitUseCase.execute(object : DisposableSingleObserver<OutfitModel>() {
            override fun onSuccess(t: OutfitModel) {
                view.processViewAction {
                    processSuccessSaving()
                    hideProgress()
                }
            }

            override fun onError(e: Throwable) {
                view.processViewAction {
                    processSuccessSaving()
                    hideProgress()
                }
            }
        })
    }

    override fun updateOutfit(
        token: String,
        id: Int,
        model: OutfitCreateModel,
        data: File
    ) {
        view.displayProgress()

        updateOutfitUseCase.initParams(
            token = token,
            outfitId = id,
            file = data,
            oufitModel = model
        )
        updateOutfitUseCase.execute(object : DisposableSingleObserver<OutfitModel>() {
            override fun onSuccess(t: OutfitModel) {
                view.processViewAction {
                    processSuccessSaving()
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
}