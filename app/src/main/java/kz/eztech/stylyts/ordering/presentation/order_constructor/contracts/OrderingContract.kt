package kz.eztech.stylyts.ordering.presentation.order_constructor.contracts

import kz.eztech.stylyts.ordering.data.models.order.OrderCreateApiModel
import kz.eztech.stylyts.ordering.data.db.cart.CartEntity
import kz.eztech.stylyts.ordering.domain.models.order.ResponseOrderCreateModel
import kz.eztech.stylyts.global.presentation.base.BasePresenter
import kz.eztech.stylyts.global.presentation.base.BaseView

interface OrderingContract {

    interface View : BaseView {

        fun processGoods(list: List<CartEntity>)

        fun processSuccessCreating(orderModel: ResponseOrderCreateModel)
    }

    interface Presenter : BasePresenter<View> {

        fun getGoodsFromCart()

        fun createOrders(orderList: List<OrderCreateApiModel>)

        fun clearCart(cartId: Int)
    }
}