package kz.eztech.stylyts.presentation.contracts.ordering

import kz.eztech.stylyts.domain.models.common.ResultsModel
import kz.eztech.stylyts.domain.models.order.OrderModel
import kz.eztech.stylyts.presentation.base.BasePresenter
import kz.eztech.stylyts.presentation.base.BaseView

interface OrderListContract {

    interface View : BaseView {

        fun processOrderList(resultsModel: ResultsModel<OrderModel>)
    }

    interface Presenter : BasePresenter<View> {

        fun getOrderList(token: String)
    }
}