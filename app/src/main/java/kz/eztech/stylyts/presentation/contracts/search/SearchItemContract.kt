package kz.eztech.stylyts.presentation.contracts.search

import kz.eztech.stylyts.data.api.models.ResultsApiModel
import kz.eztech.stylyts.data.db.search.UserSearchEntity
import kz.eztech.stylyts.data.api.models.user.UserApiModel
import kz.eztech.stylyts.presentation.base.BasePresenter
import kz.eztech.stylyts.presentation.base.BaseView

interface SearchItemContract {

    interface View : BaseView {
        fun processSearch(resultsModel: ResultsApiModel<UserApiModel>)

        fun processUserFromLocalDb(userList: List<UserSearchEntity>)
    }

    interface Presenter : BasePresenter<View> {
        fun getUserByUsername(token: String, username: String)

        fun getUserFromLocaleDb()

        fun saveUserToLocaleDb(user: UserApiModel)

        fun deleteUserFromLocalDb(userId: Int)
    }
}