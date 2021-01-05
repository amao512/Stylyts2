package kz.eztech.stylyts.presentation.contracts.main.collections

import kz.eztech.stylyts.presentation.base.BasePresenter
import kz.eztech.stylyts.presentation.base.BaseView

/**
 * Created by Ruslan Erdenoff on 25.11.2020.
 */
interface CollectionItemContract {
    interface View : BaseView {

    }
    interface Presenter: BasePresenter<View> {

    }
}