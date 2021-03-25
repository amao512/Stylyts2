package kz.eztech.stylyts.presentation.contracts.search

import kz.eztech.stylyts.presentation.base.BasePresenter
import kz.eztech.stylyts.presentation.base.BaseView

interface SearchContract {

    interface View : BaseView

    interface Presenter : BasePresenter<View>
}