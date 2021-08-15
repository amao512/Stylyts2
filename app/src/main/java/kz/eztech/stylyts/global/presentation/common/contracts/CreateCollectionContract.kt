package kz.eztech.stylyts.global.presentation.common.contracts

import kz.eztech.stylyts.global.presentation.base.BasePresenter
import kz.eztech.stylyts.global.presentation.base.BaseView

/**
 * Created by Ruslan Erdenoff on 25.11.2020.
 */
interface CreateCollectionContract {
    interface View : BaseView {

    }
    interface Presenter: BasePresenter<View> {

    }
}