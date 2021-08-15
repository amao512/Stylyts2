package kz.eztech.stylyts.collections.presentation.contracts

import kz.eztech.stylyts.global.presentation.base.BasePresenter
import kz.eztech.stylyts.global.presentation.base.BaseView
import kz.eztech.stylyts.utils.Paginator

/**
 * Created by Ruslan Erdenoff on 25.11.2020.
 */
interface CollectionItemContract {

    interface View : BaseView {

        fun getTokenId(): String

        fun renderPaginatorState(state: Paginator.State)

        fun processPostResults(list: List<Any?>)
    }

    interface Presenter : BasePresenter<View> {

        fun loadPage(page: Int)

        fun getPosts()

        fun loadMorePost()
    }
}