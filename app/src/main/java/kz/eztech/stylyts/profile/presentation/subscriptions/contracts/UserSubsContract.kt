package kz.eztech.stylyts.profile.presentation.subscriptions.contracts

import kz.eztech.stylyts.global.domain.models.common.ResultsModel
import kz.eztech.stylyts.global.domain.models.user.FollowSuccessModel
import kz.eztech.stylyts.global.domain.models.user.FollowerModel
import kz.eztech.stylyts.global.presentation.base.BasePresenter
import kz.eztech.stylyts.global.presentation.base.BaseView
import kz.eztech.stylyts.utils.EMPTY_STRING

interface UserSubsContract {

    interface View: BaseView {

        fun processFollowers(resultsModel: ResultsModel<FollowerModel>)

        fun processFollowings(resultsModel: ResultsModel<FollowerModel>)

        fun processSuccessFollowing(followSuccessModel: FollowSuccessModel)

        fun processSuccessUnFollowing(followerId: Int)
    }

    interface Presenter: BasePresenter<View> {

        fun getFollowers(
            userId: Int,
            username: String = EMPTY_STRING
        )

        fun getFollowings(
            userId: Int,
            username: String = EMPTY_STRING
        )

        fun followUser(followerId: Int)

        fun unFollowUser(followerId: Int)
    }
}