package kz.eztech.stylyts.presentation.contracts.shop

import kz.eztech.stylyts.domain.models.common.SearchFilterModel
import kz.eztech.stylyts.presentation.base.BasePresenter
import kz.eztech.stylyts.presentation.base.BaseView
import kz.eztech.stylyts.utils.Paginator

interface ShopListContract {

    interface View : BaseView {

        fun getCurrendId(): Int

        fun getSearchFilter(): SearchFilterModel

        fun renderPaginatorState(state: Paginator.State)

        fun processShops(list: List<Any?>)

        fun processCharacter(character: List<String>)
    }

    interface Presenter : BasePresenter<View> {

        fun loadPage(page: Int)

        fun loadMorePage()

        fun getShops()
    }
}