package kz.eztech.stylyts.ordering.presentation.order.contracts

import kz.eztech.stylyts.ordering.domain.models.order.OrderModel
import kz.eztech.stylyts.global.presentation.base.BasePresenter
import kz.eztech.stylyts.global.presentation.base.BaseView

interface ShopOrderDetailContract {

    interface View : BaseView {

        fun processOrder(orderModel: OrderModel)
    }

    interface Presenter : BasePresenter<View> {

        fun getOrder(orderId: Int)

        fun setStatusInProgress(orderId: Int)

        fun setStatusDelivered(orderId: Int)
    }
}