package kz.eztech.stylyts.presentation.presenters.ordering

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kz.eztech.stylyts.data.db.cart.CartDataSource
import kz.eztech.stylyts.data.exception.ErrorHelper
import kz.eztech.stylyts.presentation.base.processViewAction
import kz.eztech.stylyts.presentation.contracts.ordering.SelectDeliveryWayContract
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