package kz.eztech.stylyts.presentation.contracts.profile

import kz.eztech.stylyts.presentation.base.BasePresenter
import kz.eztech.stylyts.presentation.base.BaseView

/**
 * Created by Ruslan Erdenoff on 02.03.2021.
 */
interface CardContract {
    interface View: BaseView {
        fun displayContent()
        fun displayForm()
    }
    interface Presenter: BasePresenter<View>
}