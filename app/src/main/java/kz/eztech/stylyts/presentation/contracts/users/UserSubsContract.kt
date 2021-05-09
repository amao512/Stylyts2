package kz.eztech.stylyts.presentation.contracts.users

import kz.eztech.stylyts.domain.models.ResultsModel
import kz.eztech.stylyts.domain.models.user.FollowSuccessModel
import kz.eztech.stylyts.domain.models.user.FollowerModel
import kz.eztech.stylyts.presentation.base.BasePresenter
import kz.eztech.stylyts.presentation.base.BaseView

interface UserSubsContract {

    interface View: BaseView {

        fun processFollowers(resultsModel: ResultsModel<FollowerModel>)

        fun processFollowings(resultsModel: ResultsModel<FollowerModel>)

        fun processSuccessFollowing(followSuccessModel: FollowSuccessModel)

        fun processSuccessUnFollowing(followerId: Int)
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

        fun followUser(
            token: String,
            followerId: Int
        )

        fun unFollowUser(
            token: String,
            followerId: Int
        )
    }
}