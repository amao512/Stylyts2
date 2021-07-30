package kz.eztech.stylyts.presentation.contracts.incomes

import kz.eztech.stylyts.presentation.base.BasePresenter
import kz.eztech.stylyts.presentation.base.BaseView

/**
 * Created by Ruslan Erdenoff on 23.12.2020.
 */
interface IncomeDetailContract {

    interface View: BaseView {}

    interface Presenter: BasePresenter<View> {}
}