package kz.eztech.stylyts.global.presentation.common.contracts

import kz.eztech.stylyts.global.presentation.base.BasePresenter
import kz.eztech.stylyts.global.presentation.base.BaseView

/**
 * Created by Ruslan Erdenoff on 22.01.2021.
 */
interface EmptyContract {

    interface View: BaseView {}

    interface Presenter: BasePresenter<View> {}
}