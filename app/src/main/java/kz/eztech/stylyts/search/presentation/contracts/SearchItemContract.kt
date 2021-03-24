package kz.eztech.stylyts.search.presentation.contracts

import kz.eztech.stylyts.search.data.db.UserSearchEntity
import kz.eztech.stylyts.search.domain.models.SearchModel
import kz.eztech.stylyts.common.domain.models.UserModel
import kz.eztech.stylyts.common.presentation.base.BasePresenter
import kz.eztech.stylyts.common.presentation.base.BaseView

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