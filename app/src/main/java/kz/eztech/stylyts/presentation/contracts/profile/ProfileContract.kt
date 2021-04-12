package kz.eztech.stylyts.presentation.contracts.profile

import kz.eztech.stylyts.data.api.models.ResultsApiModel
import kz.eztech.stylyts.domain.models.CollectionFilterModel
import kz.eztech.stylyts.domain.models.PublicationModel
import kz.eztech.stylyts.domain.models.ResultsModel
import kz.eztech.stylyts.domain.models.user.FollowSuccessModel
import kz.eztech.stylyts.domain.models.user.FollowerModel
import kz.eztech.stylyts.domain.models.user.UserModel
import kz.eztech.stylyts.presentation.base.BasePresenter
import kz.eztech.stylyts.presentation.base.BaseView

/**
 * Created by Ruslan Erdenoff on 25.11.2020.
 */
interface ProfileContract {

    interface View : BaseView {

        fun showMyData()

        fun hideMyData()

        fun processMyData()

        fun processProfile(userModel: UserModel)

        fun processFilter(filterList: List<CollectionFilterModel>)

        fun processMyPublications(resultsModel: ResultsApiModel<PublicationModel>)

        fun processFollowers(resultsModel: ResultsModel<FollowerModel>)

        fun processFollowings(resultsModel: ResultsModel<FollowerModel>)

        fun processSuccessFollowing(followSuccessModel: FollowSuccessModel)

        fun processSuccessUnfollowing()
    }

    interface Presenter : BasePresenter<View> {
        fun getProfile(
            token: String,
            userId: String,
            isOwnProfile: Boolean
        )

        fun getFilerList(isOwnProfile: Boolean)

        fun getPublications(
            token: String,
            isOwnProfile: Boolean
        )

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
            userId: Int
        )

        fun unfollowUser(
            token: String,
            userId: Int
        )
    }
}