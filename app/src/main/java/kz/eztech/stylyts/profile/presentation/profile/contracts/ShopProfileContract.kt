package kz.eztech.stylyts.profile.presentation.profile.contracts

import kz.eztech.stylyts.global.domain.models.clothes.ClothesFilterModel
import kz.eztech.stylyts.global.domain.models.common.ResultsModel
import kz.eztech.stylyts.global.domain.models.filter.CollectionFilterModel
import kz.eztech.stylyts.global.domain.models.outfits.OutfitFilterModel
import kz.eztech.stylyts.global.domain.models.user.FollowSuccessModel
import kz.eztech.stylyts.global.domain.models.user.FollowerModel
import kz.eztech.stylyts.global.domain.models.user.UserModel
import kz.eztech.stylyts.global.presentation.base.BasePresenter
import kz.eztech.stylyts.global.presentation.base.BaseView
import kz.eztech.stylyts.utils.Paginator

/**
 * Created by Ruslan Erdenoff on 21.12.2020.
 */
interface ShopProfileContract {

    interface View: BaseView {

        fun getUserId(): Int

        fun processProfile(userModel: UserModel)

        fun processFollowers(resultsModel: ResultsModel<FollowerModel>)

        fun processCollectionFilter(filterList: List<CollectionFilterModel>)

        fun renderPaginatorState(state: Paginator.State)

        fun getCollectionMode(): Int

        fun getOutfitFilter(): OutfitFilterModel

        fun getClothesFilter(): ClothesFilterModel

        fun processCollections(list: List<Any?>)

        fun processSuccessFollowing(followSuccessModel: FollowSuccessModel)

        fun processSuccessUnfollowing()
    }

    interface Presenter: BasePresenter<View> {

        fun getProfile()

        fun getFollowers()

        fun getTypes()

        fun loadPage(page: Int)

        fun loadPostsPage(page: Int)

        fun loadOutfitsPage(page: Int)

        fun loadClothesPage(page: Int)

        fun getCollections()

        fun loadMorePage()

        fun onFollow()

        fun onUnFollow()
    }
}