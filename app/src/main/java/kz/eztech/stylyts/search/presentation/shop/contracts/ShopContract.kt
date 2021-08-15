package kz.eztech.stylyts.search.presentation.shop.contracts

import kz.eztech.stylyts.global.presentation.base.BasePresenter
import kz.eztech.stylyts.global.presentation.base.BaseView

/**
 * Created by Ruslan Erdenoff on 25.11.2020.
 */
interface ShopContract {
    interface View : BaseView {

    }
    interface Presenter: BasePresenter<View> {

    }
}