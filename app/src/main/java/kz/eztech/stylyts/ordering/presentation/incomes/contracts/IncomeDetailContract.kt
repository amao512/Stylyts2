package kz.eztech.stylyts.ordering.presentation.incomes.contracts

import kz.eztech.stylyts.global.presentation.base.BasePresenter
import kz.eztech.stylyts.global.presentation.base.BaseView

/**
 * Created by Ruslan Erdenoff on 23.12.2020.
 */
interface IncomeDetailContract {

    interface View: BaseView {}

    interface Presenter: BasePresenter<View> {}
}