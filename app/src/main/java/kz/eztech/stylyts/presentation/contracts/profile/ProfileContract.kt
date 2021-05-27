package kz.eztech.stylyts.presentation.contracts.profile

import kz.eztech.stylyts.domain.models.clothes.ClothesFilterModel
import kz.eztech.stylyts.domain.models.clothes.ClothesModel
import kz.eztech.stylyts.domain.models.common.ResultsModel
import kz.eztech.stylyts.domain.models.filter.CollectionFilterModel
import kz.eztech.stylyts.domain.models.outfits.OutfitFilterModel
import kz.eztech.stylyts.domain.models.outfits.OutfitModel
import kz.eztech.stylyts.domain.models.posts.PostFilterModel
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

        fun processWardrobeCount(count: Int)
    }

    interface Presenter : BasePresenter<View> {
        fun getProfile(
            token: String,
            userId: Int
        )

        fun getFilerList(isOwnProfile: Boolean)

        fun getPosts(
            token: String,
            filterModel: PostFilterModel
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

        fun getWardrobeCount(
            token: String,
            filterModel: ClothesFilterModel
        )

        fun getWardrobe(
            token: String,
            filterModel: ClothesFilterModel
        )

        fun getOutfits(
            token: String,
            filterModel: OutfitFilterModel
        )
    }
}