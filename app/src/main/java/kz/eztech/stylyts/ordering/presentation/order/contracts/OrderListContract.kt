package kz.eztech.stylyts.ordering.presentation.order.contracts

import kz.eztech.stylyts.global.presentation.base.BasePresenter
import kz.eztech.stylyts.global.presentation.base.BaseView
import kz.eztech.stylyts.utils.Paginator

interface OrderListContract {

    interface View : BaseView {

        fun renderPaginatorState(state: Paginator.State)

        fun processOrders(list: List<Any?>)
    }

    interface Presenter : BasePresenter<View> {

        fun loadPage(page: Int)

        fun loadMorePage()

        fun getOrders()
    }
}