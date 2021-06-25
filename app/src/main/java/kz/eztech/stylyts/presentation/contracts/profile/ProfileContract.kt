package kz.eztech.stylyts.presentation.contracts.profile

import kz.eztech.stylyts.domain.models.clothes.ClothesFilterModel
import kz.eztech.stylyts.domain.models.common.ResultsModel
import kz.eztech.stylyts.domain.models.filter.CollectionFilterModel
import kz.eztech.stylyts.domain.models.outfits.OutfitFilterModel
import kz.eztech.stylyts.domain.models.user.FollowSuccessModel
import kz.eztech.stylyts.domain.models.user.FollowerModel
import kz.eztech.stylyts.domain.models.user.UserModel
import kz.eztech.stylyts.presentation.base.BasePresenter
import kz.eztech.stylyts.presentation.base.BaseView
import kz.eztech.stylyts.presentation.utils.Paginator

/**
 * Created by Ruslan Erdenoff on 25.11.2020.
 */
interface ProfileContract {

    interface View : BaseView {

        fun getToken(): String

        fun getCollectionMode(): Int

        fun getUserId(): Int

        fun getClothesFilter(): ClothesFilterModel

        fun getOutfitFilter(): OutfitFilterModel

        fun navigateToMyData()

        fun processProfile(userModel: UserModel)

        fun processFilter(filterList: List<CollectionFilterModel>)

        fun renderPaginatorState(state: Paginator.State)

        fun processCollections(list: List<Any?>)

        fun processFollowers(resultsModel: ResultsModel<FollowerModel>)

        fun processSuccessFollowing(followSuccessModel: FollowSuccessModel)

        fun processSuccessUnfollowing()

        fun processWardrobeCount(count: Int)
    }

    interface Presenter : BasePresenter<View> {

        fun getProfile()

        fun getFilerList(isOwnProfile: Boolean)

        fun loadPage(page: Int)

        fun loadMoreList()

        fun loadPosts(page: Int)

        fun loadWardrobes(page: Int)

        fun loadOutfits(page: Int)

        fun getCollections()

        fun getFollowers()

        fun followUser()

        fun unfollowUser()

        fun getWardrobeCount()
    }
}