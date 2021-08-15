package kz.eztech.stylyts.ordering.presentation.incomes.contracts

import kz.eztech.stylyts.global.presentation.base.BasePresenter
import kz.eztech.stylyts.global.presentation.base.BaseView
import kz.eztech.stylyts.ordering.presentation.incomes.data.models.IncomeListItem
import kz.eztech.stylyts.ordering.presentation.incomes.data.models.ReferralItem

/**
 * Created by Ruslan Erdenoff on 23.12.2020.
 */
interface IncomeDetailContract {

    interface View: BaseView {

        fun processReferralList(list: List<ReferralItem>)
    }

    interface Presenter: BasePresenter<View> {

        fun getReferralList(incomeListItem: IncomeListItem)
    }
}