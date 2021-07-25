package kz.eztech.stylyts.presentation.presenters.collection_constructor

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import kz.eztech.stylyts.data.db.cart.CartDataSource
import kz.eztech.stylyts.data.db.cart.CartMapper
import kz.eztech.stylyts.data.exception.ErrorHelper
import kz.eztech.stylyts.domain.models.outfits.OutfitCreateModel
import kz.eztech.stylyts.domain.models.posts.PostCreateModel
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
    private val cartDataSource: CartDataSource
) : CreateCollectionAcceptContract.Presenter {

    private lateinit var view: CreateCollectionAcceptContract.View

    private val disposables = CompositeDisposable()

    override fun disposeRequests() {
        createPostUseCase.clear()
        updatePostUseCase.clear()
        createOutfitUseCase.clear()
        updateOutfitUseCase.clear()
    }

    override fun attach(view: CreateCollectionAcceptContract.View) {
        this.view = view
    }

    override fun createPost(postCreateModel: PostCreateModel) {
        view.displayProgress()

        createPostUseCase.initParams(postCreateModel)
        createPostUseCase.execute(object : DisposableSingleObserver<PostCreateModel>() {
            override fun onSuccess(t: PostCreateModel) {
                view.processViewAction {
                    hideProgress()
                    processSuccessSavingPost(postModel = t)
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

    override fun updatePost(
        postId: Int,
        postCreateModel: PostCreateModel
    ) {
        view.displayProgress()

        updatePostUseCase.initParams(postId, postCreateModel)
        updatePostUseCase.execute(object : DisposableSingleObserver<PostCreateModel>() {
            override fun onSuccess(t: PostCreateModel) {
                view.processViewAction {
                    hideProgress()
                    processSuccessUpdatingPost(postModel = t)
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

    override fun createOutfit(model: OutfitCreateModel, data: File) {
        view.displayProgress()

        createOutfitUseCase.initParam(
            file = data,
            outfitCreateModel = model
        )
        createOutfitUseCase.execute(object : DisposableSingleObserver<OutfitCreateModel>() {
            override fun onSuccess(t: OutfitCreateModel) {
                view.processViewAction {
                    processSuccessSavingOutfit(outfitModel = t)
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

    override fun updateOutfit(
        id: Int,
        model: OutfitCreateModel,
        data: File
    ) {
        view.displayProgress()

        updateOutfitUseCase.initParams(
            outfitId = id,
            file = data,
            outfitModel = model
        )
        updateOutfitUseCase.execute(object : DisposableSingleObserver<OutfitCreateModel>() {
            override fun onSuccess(t: OutfitCreateModel) {
                view.processViewAction {
                    processSuccessSavingOutfit(outfitModel = t)
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

    override fun saveToCart(outfitCreateModel: OutfitCreateModel) {
        disposables.clear()
        disposables.add(


            cartDataSource.insertAll(list = CartMapper.map(list = outfitCreateModel.clothes))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    view.processSuccessSavingToCart()
                }, {
                    view.displayMessage(msg = errorHelper.processError(it))
                })
        )
    }
}