package kz.eztech.stylyts.presentation.contracts.profile

import kz.eztech.stylyts.domain.models.common.ResultsModel
import kz.eztech.stylyts.domain.models.common.SearchFilterModel
import kz.eztech.stylyts.domain.models.user.UserModel
import kz.eztech.stylyts.presentation.base.BasePresenter
import kz.eztech.stylyts.presentation.base.BaseView

/**
 * Created by Ruslan Erdenoff on 22.02.2021.
 */
interface UserSearchContract {

    interface View : BaseView {

        fun processUserResults(resultsModel: ResultsModel<UserModel>)
    }

    interface Presenter : BasePresenter<View> {
        fun getUserByUsername(
            token: String,
            searchFilterModel: SearchFilterModel
        )
    }
}