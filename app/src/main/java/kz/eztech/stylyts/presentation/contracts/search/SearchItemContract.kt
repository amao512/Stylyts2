package kz.eztech.stylyts.presentation.contracts.search

import kz.eztech.stylyts.data.db.search.UserSearchEntity
import kz.eztech.stylyts.domain.models.search.SearchModel
import kz.eztech.stylyts.domain.models.UserModel
import kz.eztech.stylyts.presentation.base.BasePresenter
import kz.eztech.stylyts.presentation.base.BaseView

interface SearchItemContract {

    interface View : BaseView {
        fun processSearch(searchModel: SearchModel<UserModel>)

        fun processUserFromLocalDb(userList: List<UserSearchEntity>)
    }

    interface Presenter : BasePresenter<View> {
        fun getUserByUsername(token: String, username: String)

        fun getUserFromLocaleDb()

        fun saveUserToLocaleDb(user: UserModel)

        fun deleteUserFromLocalDb(userId: Int)
    }
}