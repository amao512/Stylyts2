package kz.eztech.stylyts.presentation.contracts.ordering

import kz.eztech.stylyts.presentation.base.BasePresenter
import kz.eztech.stylyts.presentation.base.BaseView
import kz.eztech.stylyts.presentation.utils.Paginator

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