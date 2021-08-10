package kz.eztech.stylyts.presentation.contracts.ordering

import kz.eztech.stylyts.data.api.models.order.OrderCreateApiModel
import kz.eztech.stylyts.data.db.cart.CartEntity
import kz.eztech.stylyts.domain.models.order.OrderCreateModel
import kz.eztech.stylyts.domain.models.order.ResponseOrderCreateModel
import kz.eztech.stylyts.presentation.base.BasePresenter
import kz.eztech.stylyts.presentation.base.BaseView

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