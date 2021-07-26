package kz.eztech.stylyts.presentation.contracts.incomes

import kz.eztech.stylyts.presentation.base.BasePresenter
import kz.eztech.stylyts.presentation.base.BaseView
import kz.eztech.stylyts.presentation.utils.Paginator

/**
 * Created by Ruslan Erdenoff on 23.12.2020.
 */
interface IncomeContract {

    interface View : BaseView {

        fun renderPaginatorState(state: Paginator.State)

        fun processReferrals(list: List<Any?>)
    }

    interface Presenter: BasePresenter<View> {

        fun loadPage(page: Int)

        fun getReferrals()

        fun loadMorePage()
    }
}