package kz.eztech.stylyts.presentation.contracts.main.shop

import kz.eztech.stylyts.presentation.base.BasePresenter
import kz.eztech.stylyts.presentation.base.BaseView

/**
 * Created by Ruslan Erdenoff on 25.11.2020.
 */
interface ShopContract {
    interface View : BaseView {

    }
    interface Presenter: BasePresenter<View> {

    }
}