package kz.eztech.stylyts.presentation.contracts.main.constructor


import kz.eztech.stylyts.common.presentation.base.BasePresenter
import kz.eztech.stylyts.common.presentation.base.BaseView

/**
 * Created by Ruslan Erdenoff on 22.01.2021.
 */
interface ConstructorHolderContract {
    interface View: BaseView {

    }
    interface Presenter: BasePresenter<View> {

    }
}