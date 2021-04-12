package kz.eztech.stylyts.presentation.contracts.users

import kz.eztech.stylyts.domain.models.ResultsModel
import kz.eztech.stylyts.domain.models.user.FollowerModel
import kz.eztech.stylyts.presentation.base.BasePresenter
import kz.eztech.stylyts.presentation.base.BaseView

interface UserSubsContract {

    interface View: BaseView {

        fun processFollowers(resultsModel: ResultsModel<FollowerModel>)

        fun processFollowings(resultsModel: ResultsModel<FollowerModel>)
    }

    interface Presenter: BasePresenter<View> {

        fun getFollowers(
            token: String,
            userId: Int
        )

        fun getFollowings(
            token: String,
            userId: Int
        )
    }
}