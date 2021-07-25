package kz.eztech.stylyts.presentation.contracts.profile

import kz.eztech.stylyts.domain.models.common.SearchFilterModel
import kz.eztech.stylyts.presentation.base.BasePresenter
import kz.eztech.stylyts.presentation.base.BaseView
import kz.eztech.stylyts.presentation.utils.Paginator

/**
 * Created by Ruslan Erdenoff on 22.02.2021.
 */
interface UserSearchContract {

    interface View : BaseView {

        fun getSearchFilter(): SearchFilterModel

        fun renderPaginatorState(state: Paginator.State)

        fun processUserResults(list: List<Any?>)
    }

    interface Presenter : BasePresenter<View> {

        fun loadPage(page: Int)

        fun loadMorePage()

        fun getUsers()
    }
}