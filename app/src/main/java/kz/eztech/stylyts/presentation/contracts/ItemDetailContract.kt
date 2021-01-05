package kz.eztech.stylyts.presentation.contracts

import kz.eztech.stylyts.presentation.base.BasePresenter
import kz.eztech.stylyts.presentation.base.BaseView

/**
 * Created by Ruslan Erdenoff on 04.12.2020.
 */
interface ItemDetailContract {
    interface View: BaseView {

    }

    interface Presenter: BasePresenter<View> {

    }
}