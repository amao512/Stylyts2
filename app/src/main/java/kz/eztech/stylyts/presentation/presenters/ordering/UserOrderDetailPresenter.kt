package kz.eztech.stylyts.presentation.presenters.ordering

import io.reactivex.observers.DisposableSingleObserver
import kz.eztech.stylyts.data.exception.ErrorHelper
import kz.eztech.stylyts.domain.models.order.OrderModel
import kz.eztech.stylyts.domain.usecases.order.GetOrderByIdUseCase
import kz.eztech.stylyts.presentation.base.processViewAction
import kz.eztech.stylyts.presentation.contracts.ordering.UserOrderDetailContract
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

    override fun getOrderById(
        token: String,
        orderId: Int
    ) {
        view.displayProgress()

        getOrderByIdUseCase.initParams(token, orderId)
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