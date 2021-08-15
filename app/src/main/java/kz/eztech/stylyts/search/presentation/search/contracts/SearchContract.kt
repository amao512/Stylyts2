package kz.eztech.stylyts.search.presentation.search.contracts

import kz.eztech.stylyts.global.presentation.base.BasePresenter
import kz.eztech.stylyts.global.presentation.base.BaseView

interface SearchContract {

    interface View : BaseView

    interface Presenter : BasePresenter<View>
}