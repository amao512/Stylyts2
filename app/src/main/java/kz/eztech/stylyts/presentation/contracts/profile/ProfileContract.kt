package kz.eztech.stylyts.presentation.contracts.profile

import kz.eztech.stylyts.domain.models.filter.CollectionFilterModel
import kz.eztech.stylyts.domain.models.ResultsModel
import kz.eztech.stylyts.domain.models.clothes.ClothesModel
import kz.eztech.stylyts.domain.models.filter.FilterModel
import kz.eztech.stylyts.domain.models.outfits.OutfitModel
import kz.eztech.stylyts.domain.models.posts.PostModel
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

        fun navigateToMyData()

        fun processProfile(userModel: UserModel)

        fun processFilter(filterList: List<CollectionFilterModel>)

        fun processPostResults(resultsModel: ResultsModel<PostModel>)

        fun processFollowers(resultsModel: ResultsModel<FollowerModel>)

        fun processSuccessFollowing(followSuccessModel: FollowSuccessModel)

        fun processSuccessUnfollowing()

        fun processWardrobeResults(resultsModel: ResultsModel<ClothesModel>)

        fun processOutfitResults(resultsModel: ResultsModel<OutfitModel>)
    }

    interface Presenter : BasePresenter<View> {
        fun getProfile(
            token: String,
            userId: String,
            isOwnProfile: Boolean
        )

        fun getFilerList(isOwnProfile: Boolean)

        fun getCollections(
            token: String,
            mode: Int,
            filterModel: FilterModel
        )

        fun getPosts(
            token: String,
            filterModel: FilterModel
        )

        fun getFollowers(
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

        fun getWardrobe(
            token: String,
            filterModel: FilterModel
        )

        fun getOutfits(
            token: String,
            filterModel: FilterModel
        )
    }
}