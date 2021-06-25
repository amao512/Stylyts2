package kz.eztech.stylyts.presentation.contracts.main.shop

import kz.eztech.stylyts.domain.models.clothes.ClothesFilterModel
import kz.eztech.stylyts.domain.models.clothes.ClothesModel
import kz.eztech.stylyts.domain.models.clothes.ClothesTypeModel
import kz.eztech.stylyts.domain.models.common.ResultsModel
import kz.eztech.stylyts.domain.models.outfits.OutfitFilterModel
import kz.eztech.stylyts.domain.models.outfits.OutfitModel
import kz.eztech.stylyts.domain.models.posts.PostFilterModel
import kz.eztech.stylyts.domain.models.posts.PostModel
import kz.eztech.stylyts.domain.models.user.FollowSuccessModel
import kz.eztech.stylyts.domain.models.user.FollowerModel
import kz.eztech.stylyts.domain.models.user.UserModel
import kz.eztech.stylyts.presentation.base.BasePresenter
import kz.eztech.stylyts.presentation.base.BaseView
import kz.eztech.stylyts.presentation.utils.Paginator

/**
 * Created by Ruslan Erdenoff on 21.12.2020.
 */
interface ShopProfileContract {

    interface View: BaseView {

        fun getToken(): String

        fun getUserId(): Int

        fun processProfile(userModel: UserModel)

        fun processFollowers(resultsModel: ResultsModel<FollowerModel>)

        fun processTypes(resultsModel: ResultsModel<ClothesTypeModel>)

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