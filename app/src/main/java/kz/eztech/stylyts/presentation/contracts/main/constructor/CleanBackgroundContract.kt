package kz.eztech.stylyts.presentation.contracts.main.constructor

import kz.eztech.stylyts.presentation.base.BasePresenter
import kz.eztech.stylyts.presentation.base.BaseView

/**
 * Created by Ruslan Erdenoff on 10.02.2021.
 */
interface CleanBackgroundContract {
    interface View:BaseView{}
    interface Presenter: BasePresenter<View> {}
}