package kz.eztech.stylyts.presentation.contracts.ordering

import kz.eztech.stylyts.domain.models.order.OrderModel
import kz.eztech.stylyts.presentation.base.BasePresenter
import kz.eztech.stylyts.presentation.base.BaseView

interface PaymentContract {

    interface View : BaseView {

        fun processOrder(orderModel: OrderModel)
    }

    interface Presenter : BasePresenter<View> {

        fun getOrderById(orderId: Int)
    }
}