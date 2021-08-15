package kz.eztech.stylyts.ordering.presentation.order_constructor.contracts

import kz.eztech.stylyts.ordering.data.db.cart.CartEntity
import kz.eztech.stylyts.global.presentation.base.BasePresenter
import kz.eztech.stylyts.global.presentation.base.BaseView

interface SelectDeliveryWayContract {

    interface View : BaseView {

        fun processGoods(list: List<CartEntity>)
    }

    interface Presenter : BasePresenter<View> {

        fun getGoodsFromCart()
    }
}