package kz.eztech.stylyts.presentation.contracts.main.shop

import kz.eztech.stylyts.domain.models.clothes.ClothesFilterModel
import kz.eztech.stylyts.domain.models.clothes.ClothesModel
import kz.eztech.stylyts.domain.models.clothes.ClothesTypeModel
import kz.eztech.stylyts.domain.models.common.ResultsModel
import kz.eztech.stylyts.domain.models.outfits.OutfitFilterModel
import kz.eztech.stylyts.domain.models.outfits.OutfitModel
import kz.eztech.stylyts.domain.models.user.FollowSuccessModel
import kz.eztech.stylyts.domain.models.user.FollowerModel
import kz.eztech.stylyts.domain.models.user.UserModel
import kz.eztech.stylyts.presentation.base.BasePresenter
import kz.eztech.stylyts.presentation.base.BaseView

/**
 * Created by Ruslan Erdenoff on 21.12.2020.
 */
interface ShopProfileContract {

    interface View: BaseView {

        fun processProfile(userModel: UserModel)

        fun processFollowers(resultsModel: ResultsModel<FollowerModel>)

        fun processTypes(resultsModel: ResultsModel<ClothesTypeModel>)

        fun processOutfits(resultsModel: ResultsModel<OutfitModel>)

        fun processClothes(resultsModel: ResultsModel<ClothesModel>)

        fun processSuccessFollowing(followSuccessModel: FollowSuccessModel)

        fun processSuccessUnfollowing()
    }

    interface Presenter: BasePresenter<View> {

        fun getProfile(
            token: String,
            id: Int
        )

        fun getFollowers(
            token: String,
            userId: Int
        )

        fun getTypes(token: String)

        fun getOutfits(
            token: String,
            filterModel: OutfitFilterModel
        )

        fun getClothes(
            token: String,
            filterModel: ClothesFilterModel
        )

        fun onFollow(
            token: String,
            userId: Int
        )

        fun onUnFollow(
            token: String,
            userId: Int
        )
    }
}