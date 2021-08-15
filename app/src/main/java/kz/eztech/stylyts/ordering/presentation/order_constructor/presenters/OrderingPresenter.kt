package kz.eztech.stylyts.ordering.presentation.order_constructor.presenters

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import kz.eztech.stylyts.ordering.data.models.order.OrderCreateApiModel
import kz.eztech.stylyts.ordering.data.db.cart.CartDataSource
import kz.eztech.stylyts.global.data.exception.ErrorHelper
import kz.eztech.stylyts.ordering.domain.models.order.ResponseOrderCreateModel
import kz.eztech.stylyts.ordering.domain.usecases.order.CreateOrderUseCase
import kz.eztech.stylyts.global.presentation.base.processViewAction
import kz.eztech.stylyts.ordering.presentation.order_constructor.contracts.OrderingContract
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