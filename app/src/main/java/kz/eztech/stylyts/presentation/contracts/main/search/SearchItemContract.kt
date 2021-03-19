package kz.eztech.stylyts.presentation.contracts.main.search

import kz.eztech.stylyts.data.db.entities.UserSearchEntity
import kz.eztech.stylyts.domain.models.SearchModel
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