package kz.eztech.stylyts.presentation.presenters.ordering

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import kz.eztech.stylyts.data.api.models.order.OrderCreateApiModel
import kz.eztech.stylyts.data.db.cart.CartDataSource
import kz.eztech.stylyts.data.exception.ErrorHelper
import kz.eztech.stylyts.domain.models.order.ResponseOrderCreateModel
import kz.eztech.stylyts.domain.usecases.order.CreateOrderUseCase
import kz.eztech.stylyts.presentation.base.processViewAction
import kz.eztech.stylyts.presentation.contracts.ordering.OrderingContract
import javax.inject.Inject

class OrderingPresenter @Inject constructor(
    private val errorHelper: ErrorHelper,
    private val cartDataSource: CartDataSource,
    private val createOrderUseCase: CreateOrderUseCase
) : OrderingContract.Presenter {

    private lateinit var view: OrderingContract.View

    private val disposable = CompositeDisposable()

    override fun disposeRequests() {
        disposable.clear()
        createOrderUseCase.clear()
    }

    override fun attach(view: OrderingContract.View) {
        this.view = view
    }

    override fun getGoodsFromCart() {
        view.displayProgress()

        disposable.clear()
        disposable.add(
            cartDataSource.allCart
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    view.processViewAction {
                        processGoods(list = it)
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

    override fun createOrders(orderList: List<OrderCreateApiModel>) {
        view.displayProgress()

        orderList.map { order ->
            createOrderUseCase.initParams(order)
            createOrderUseCase.execute(object : DisposableSingleObserver<ResponseOrderCreateModel>() {
                override fun onSuccess(t: ResponseOrderCreateModel) {
                    t.itemObjects.map { id ->
                        clearCart(cartId = id)
                    }

                    view.processSuccessCreating(orderModel = t)
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

    override fun clearCart(cartId: Int) {
        disposable.clear()
        disposable.add(
            cartDataSource.delete(cartId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe()
        )
    }
}