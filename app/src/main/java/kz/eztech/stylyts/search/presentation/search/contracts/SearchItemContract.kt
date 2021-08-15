package kz.eztech.stylyts.search.presentation.search.contracts

import kz.eztech.stylyts.global.data.db.search.UserSearchEntity
import kz.eztech.stylyts.global.domain.models.common.SearchFilterModel
import kz.eztech.stylyts.global.domain.models.user.UserModel
import kz.eztech.stylyts.global.presentation.base.BasePresenter
import kz.eztech.stylyts.global.presentation.base.BaseView
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