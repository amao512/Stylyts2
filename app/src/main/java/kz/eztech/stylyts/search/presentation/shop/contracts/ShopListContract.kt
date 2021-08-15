package kz.eztech.stylyts.search.presentation.shop.contracts

import kz.eztech.stylyts.global.domain.models.common.SearchFilterModel
import kz.eztech.stylyts.global.presentation.base.BasePresenter
import kz.eztech.stylyts.global.presentation.base.BaseView
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