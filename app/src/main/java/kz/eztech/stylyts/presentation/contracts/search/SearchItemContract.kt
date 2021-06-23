package kz.eztech.stylyts.presentation.contracts.search

import kz.eztech.stylyts.data.db.search.UserSearchEntity
import kz.eztech.stylyts.domain.models.clothes.ClothesModel
import kz.eztech.stylyts.domain.models.common.ResultsModel
import kz.eztech.stylyts.domain.models.common.SearchFilterModel
import kz.eztech.stylyts.domain.models.user.UserModel
import kz.eztech.stylyts.presentation.base.BasePresenter
import kz.eztech.stylyts.presentation.base.BaseView

interface SearchItemContract {

    interface View : BaseView {
        fun processUserResults(resultsModel: ResultsModel<UserModel>)

        fun processUserFromLocalDb(userList: List<UserSearchEntity>)

        fun processShopResults(resultsModel: ResultsModel<UserModel>)

        fun processClothesResults(resultsModel: ResultsModel<ClothesModel>)
    }

    interface Presenter : BasePresenter<View> {
        fun searchUserByUsername(
            token: String,
            searchFilterModel: SearchFilterModel
        )

        fun getUserFromLocaleDb()

        fun saveUserToLocaleDb(user: UserModel)

        fun deleteUserFromLocalDb(user: UserSearchEntity)

        fun searchShop(
            token: String,
            searchFilterModel: SearchFilterModel
        )

        fun searchClothesByTitle(
            token: String,
            searchFilterModel: SearchFilterModel
        )
    }
}