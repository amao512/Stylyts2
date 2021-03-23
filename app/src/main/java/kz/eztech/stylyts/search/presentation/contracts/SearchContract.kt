package kz.eztech.stylyts.search.presentation.contracts

import kz.eztech.stylyts.common.presentation.base.BasePresenter
import kz.eztech.stylyts.common.presentation.base.BaseView

interface SearchContract {

    interface View : BaseView

    interface Presenter : BasePresenter<View>
}