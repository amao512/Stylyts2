package kz.eztech.stylyts.collection.presentation.contracts

import kz.eztech.stylyts.common.presentation.base.BasePresenter
import kz.eztech.stylyts.common.presentation.base.BaseView

/**
 * Created by Ruslan Erdenoff on 25.11.2020.
 */
interface CollectionsContract {
    interface View : BaseView {

    }
    interface Presenter: BasePresenter<View> {

    }
}