package kz.eztech.stylyts.presentation.contracts.main

import kz.eztech.stylyts.presentation.base.BasePresenter
import kz.eztech.stylyts.presentation.base.BaseView

/**
 * Created by Ruslan Erdenoff on 25.11.2020.
 */
interface CreateCollectionContract {
    interface View : BaseView {

    }
    interface Presenter: BasePresenter<View> {

    }
}