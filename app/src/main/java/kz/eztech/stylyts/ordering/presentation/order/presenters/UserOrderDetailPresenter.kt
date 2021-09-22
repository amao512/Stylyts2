package kz.eztech.stylyts.ordering.presentation.order.presenters

import io.reactivex.observers.DisposableSingleObserver
import kz.eztech.stylyts.global.data.exception.ErrorHelper
import kz.eztech.stylyts.ordering.domain.models.order.OrderModel
import kz.eztech.stylyts.ordering.domain.usecases.order.GetOrderByIdUseCase
import kz.eztech.stylyts.global.presentation.base.processViewAction
import kz.eztech.stylyts.ordering.presentation.order.contracts.UserOrderDetailContract
import javax.inject.Inject

class UserOrderDetailPresenter @Inject constructor(
    private val errorHelper: ErrorHelper,
    private val getOrderByIdUseCase: GetOrderByIdUseCase
) : UserOrderDetailContract.Presenter {

    private lateinit var view: UserOrderDetailContract.View

    override fun disposeRequests() {
        getOrderByIdUseCase.clear()
    }

    override fun attach(view: UserOrderDetailContract.View) {
        this.view = view
    }

    override fun getOrderById(orderId: Int) {
        view.displayProgress()

        getOrderByIdUseCase.initParams(orderId)
        getOrderByIdUseCase.execute(object : DisposableSingleObserver<OrderModel>() {
            override fun onSuccess(t: OrderModel) {
                view.processViewAction {
                    processOrder(orderModel = t)
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