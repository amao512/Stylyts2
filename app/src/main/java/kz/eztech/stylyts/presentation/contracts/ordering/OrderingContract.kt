package kz.eztech.stylyts.presentation.contracts.ordering

import kz.eztech.stylyts.data.api.models.order.OrderCreateApiModel
import kz.eztech.stylyts.data.db.cart.CartEntity
import kz.eztech.stylyts.domain.models.order.OrderModel
import kz.eztech.stylyts.presentation.base.BasePresenter
import kz.eztech.stylyts.presentation.base.BaseView

interface OrderingContract {

    interface View : BaseView {

        fun processGoods(list: List<CartEntity>)

        fun processSuccessCreating(orderModel: OrderModel)
    }

    interface Presenter : BasePresenter<View> {

        fun getGoodsFromCart()

        fun createOrders(
            token: String,
            orderList: List<OrderCreateApiModel>
        )

        fun clearCart(cartId: Int)
    }
}