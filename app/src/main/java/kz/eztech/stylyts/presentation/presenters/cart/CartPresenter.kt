package kz.eztech.stylyts.presentation.presenters.cart

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import kz.eztech.stylyts.data.db.cart.CartDataSource
import kz.eztech.stylyts.data.db.cart.CartEntity
import kz.eztech.stylyts.data.exception.ErrorHelper
import kz.eztech.stylyts.domain.models.clothes.ClothesCountModel
import kz.eztech.stylyts.domain.models.clothes.ClothesModel
import kz.eztech.stylyts.domain.models.clothes.ClothesSizeModel
import kz.eztech.stylyts.domain.usecases.clothes.GetClothesByIdUseCase
import kz.eztech.stylyts.presentation.base.processViewAction
import kz.eztech.stylyts.presentation.contracts.cart.CartContract
import javax.inject.Inject

class CartPresenter @Inject constructor(
    private val errorHelper: ErrorHelper,
    private val cartDataSource: CartDataSource,
    private val getClothesByIdUseCase: GetClothesByIdUseCase
): CartContract.Presenter {

    private lateinit var view: CartContract.View

    private val disposable = CompositeDisposable()

    override fun disposeRequests() {
        disposable.clear()
        getClothesByIdUseCase.clear()
    }

    override fun attach(view: CartContract.View) {
        this.view = view
    }

    override fun getCartList() {
        view.displayProgress()

        disposable.clear()
        disposable.add(
            cartDataSource.allCart
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    view.processViewAction {
                        processCartList(list = it)
                        hideProgress()
                    }
                }, {
                    view.processViewAction {
                        hideProgress()
                        displayMessage(msg = errorHelper.processError(it))
                    }
                })
        )
    }

    override fun selectSize(clothesSizeModel: ClothesSizeModel) {
        disposable.clear()
        disposable.add(
            cartDataSource.allCart
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map { list ->
                    val cart = list.find { it.id == clothesSizeModel.clothesId }
                    cart?.size = clothesSizeModel.size
                    cart?.count = 1

                    cart?.count?.let {
                        if (clothesSizeModel.salePrice != 0) {
                            cart.salePrice = clothesSizeModel.salePrice.times(it)
                        } else {
                            cart.price = clothesSizeModel.price.times(it)
                        }
                    }

                    cart
                }
                .subscribe({ cart ->
                    cart?.let {
                        updateCart(cartEntity = it)
                    }
                }, {
                    view.displayMessage(msg = errorHelper.processError(it))
                })
        )
    }

    override fun selectCount(clothesCountModel: ClothesCountModel) {
        disposable.clear()
        disposable.add(
            cartDataSource.allCart
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map { list ->
                    val cart = list.find { it.id == clothesCountModel.clothesId }
                    cart?.count = clothesCountModel.count
                    cart?.count?.let {
                        if (clothesCountModel.salePrice != 0) {
                            cart.salePrice = clothesCountModel.salePrice.times(it)
                        } else {
                            cart.price = clothesCountModel.price.times(it)
                        }
                    }

                    cart
                }
                .subscribe({ cart ->
                    cart?.let {
                        updateCart(cartEntity = it)
                    }
                }, {
                    view.displayMessage(msg = errorHelper.processError(it))
                })
        )
    }

    override fun updateCart(cartEntity: CartEntity) {
        disposable.clear()
        disposable.add(
            cartDataSource.update(item = cartEntity)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    getCartList()
                }, {
                    view.displayMessage(msg = errorHelper.processError(it))
                })
        )
    }

    override fun removeCart(cartEntity: CartEntity) {
        disposable.clear()
        disposable.add(
            cartDataSource.delete(item = cartEntity)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    getCartList()
                }, {
                    view.displayMessage(msg = errorHelper.processError(it))
                })
        )
    }

    override fun getSizes(
        token: String,
        clothesId: Int,
        cartEntity: CartEntity,
        isSize: Boolean
    ) {
        getClothesByIdUseCase.initParams(token, clothesId)
        getClothesByIdUseCase.execute(object : DisposableSingleObserver<ClothesModel>() {
            override fun onSuccess(t: ClothesModel) {
                view.processSizes(
                    clothesModel = t,
                    cartEntity = cartEntity,
                    isSize = isSize
                )
            }

            override fun onError(e: Throwable) {
                view.displayMessage(msg = errorHelper.processError(e))
            }
        })
    }
}