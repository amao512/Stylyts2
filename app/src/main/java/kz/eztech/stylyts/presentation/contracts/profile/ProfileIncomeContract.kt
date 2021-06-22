package kz.eztech.stylyts.presentation.contracts.profile

import kz.eztech.stylyts.domain.models.common.PageFilterModel
import kz.eztech.stylyts.presentation.base.BasePresenter
import kz.eztech.stylyts.presentation.base.BaseView

/**
 * Created by Ruslan Erdenoff on 23.12.2020.
 */
interface ProfileIncomeContract {

    interface View : BaseView {

    }

    interface Presenter: BasePresenter<View> {

        fun getIncomes(
            token: String,
            pageFilterModel: PageFilterModel
        )
    }
}