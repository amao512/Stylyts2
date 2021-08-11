package kz.eztech.stylyts.presentation.contracts.search

import kz.eztech.stylyts.data.db.search.UserSearchEntity
import kz.eztech.stylyts.domain.models.common.SearchFilterModel
import kz.eztech.stylyts.domain.models.user.UserModel
import kz.eztech.stylyts.presentation.base.BasePresenter
import kz.eztech.stylyts.presentation.base.BaseView
import kz.eztech.stylyts.utils.Paginator

interface SearchItemContract {

    interface View : BaseView {

        fun getCurrentPosition(): Int

        fun getSearchFilter(): SearchFilterModel

        fun renderPaginatorState(state: Paginator.State)

        fun processList(list: List<Any?>)

        fun processUserFromLocalDb(userList: List<UserSearchEntity>)
    }

    interface Presenter : BasePresenter<View> {

        fun loadPage(page: Int)

        fun getList()

        fun searchUser(page: Int)

        fun getUserFromLocaleDb()

        fun saveUserToLocaleDb(user: UserModel)

        fun deleteUserFromLocalDb(user: UserSearchEntity)

        fun searchShop(page: Int)

        fun searchClothes(page: Int)

        fun loadMorePage()
    }
}