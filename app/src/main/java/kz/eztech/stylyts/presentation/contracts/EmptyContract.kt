package kz.eztech.stylyts.presentation.contracts

import kz.eztech.stylyts.presentation.base.BasePresenter
import kz.eztech.stylyts.presentation.base.BaseView

/**
 * Created by Ruslan Erdenoff on 22.01.2021.
 */
interface EmptyContract {
    interface View: BaseView {

    }

    interface Presenter: BasePresenter<View> {

    }
}