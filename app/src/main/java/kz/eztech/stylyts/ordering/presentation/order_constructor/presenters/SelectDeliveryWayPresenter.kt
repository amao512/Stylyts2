package kz.eztech.stylyts.ordering.presentation.order_constructor.presenters

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kz.eztech.stylyts.ordering.data.db.cart.CartDataSource
import kz.eztech.stylyts.global.data.exception.ErrorHelper
import kz.eztech.stylyts.global.presentation.base.processViewAction
import kz.eztech.stylyts.ordering.presentation.order_constructor.contracts.SelectDeliveryWayContract
import javax.inject.Inject

class SelectDeliveryWayPresenter @Inject constructor(
    private val errorHelper: ErrorHelper,
    private val cartDataSource: CartDataSource,
) : SelectDeliveryWayContract.Presenter {

    private lateinit var view: SelectDeliveryWayContract.View

    private val disposable = CompositeDisposable()

    override fun disposeRequests() {

    }

    override fun attach(view: SelectDeliveryWayContract.View) {
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
}