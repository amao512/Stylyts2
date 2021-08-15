package kz.eztech.stylyts.ordering.presentation.incomes.contracts

import kz.eztech.stylyts.global.presentation.base.BasePresenter
import kz.eztech.stylyts.global.presentation.base.BaseView
import kz.eztech.stylyts.utils.Paginator

/**
 * Created by Ruslan Erdenoff on 23.12.2020.
 */
interface IncomeContract {

    interface View : BaseView {

        fun renderPaginatorState(state: Paginator.State)

        fun processReferrals(list: List<Any?>)

        fun processTotalProfit(totalProfit: Int)
    }

    interface Presenter: BasePresenter<View> {

        fun loadPage(page: Int)

        fun getReferrals()

        fun loadMorePage()

        fun getTotalProfit(list: List<Any?>)
    }
}