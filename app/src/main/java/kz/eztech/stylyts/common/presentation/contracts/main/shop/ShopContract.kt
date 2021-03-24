package kz.eztech.stylyts.common.presentation.contracts.main.shop

import kz.eztech.stylyts.common.presentation.base.BasePresenter
import kz.eztech.stylyts.common.presentation.base.BaseView

/**
 * Created by Ruslan Erdenoff on 25.11.2020.
 */
interface ShopContract {
    interface View : BaseView {

    }
    interface Presenter: BasePresenter<View> {

    }
}