package kz.eztech.stylyts.presentation.contracts.main.detail

import kz.eztech.stylyts.common.presentation.base.BasePresenter
import kz.eztech.stylyts.common.presentation.base.BaseView

/**
 * Created by Ruslan Erdenoff on 31.01.2021.
 */
interface CollectionDetailContract {
    interface View: BaseView {}
    interface Presenter: BasePresenter<View> {}
}