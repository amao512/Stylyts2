package kz.eztech.stylyts.presentation.utils.mappers

import kz.eztech.stylyts.data.api.models.ResultsApiModel
import kz.eztech.stylyts.data.api.models.address.AddressApiModel
import kz.eztech.stylyts.data.api.models.clothes.*
import kz.eztech.stylyts.data.api.models.comments.CommentApiModel
import kz.eztech.stylyts.data.api.models.order.OrderApiModel
import kz.eztech.stylyts.data.api.models.outfits.OutfitApiModel
import kz.eztech.stylyts.data.api.models.posts.PostApiModel
import kz.eztech.stylyts.data.api.models.referrals.ReferralApiModel
import kz.eztech.stylyts.data.api.models.user.FollowerApiModel
import kz.eztech.stylyts.data.api.models.user.UserApiModel
import kz.eztech.stylyts.data.api.models.wardrobe.WardrobeApiModel
import kz.eztech.stylyts.presentation.utils.mappers.address.map
import kz.eztech.stylyts.presentation.utils.mappers.clothes.map
import kz.eztech.stylyts.presentation.utils.mappers.comments.map
import kz.eztech.stylyts.presentation.utils.mappers.order.map
import kz.eztech.stylyts.presentation.utils.mappers.outfits.map
import kz.eztech.stylyts.presentation.utils.mappers.posts.map
import kz.eztech.stylyts.presentation.utils.mappers.user.map
import kz.eztech.stylyts.presentation.utils.mappers.wardrobe.map
import kz.eztech.stylyts.domain.models.common.ResultsModel
import kz.eztech.stylyts.presentation.utils.mappers.referrals.map

/**
 * Created by Asylzhan Seytbek on 11.04.2021.
 */
inline fun <reified T, C> ResultsApiModel<T>?.map(): ResultsModel<C> {
    return ResultsModel(
        page = this?.page ?: 0,
        totalPages = this?.totalPages ?: 0,
        pageSize = this?.pageSize ?: 0,
        totalCount = this?.totalCount ?: 0,
        results = this?.results.map()
    )
}

@Suppress("UNCHECKED_CAST")
inline fun <reified T, C> List<T>?.map(): List<C> {
    return when (T::class) {
        ClothesApiModel::class -> {
            (this as List<ClothesApiModel>).map() as List<C>
        }
        ClothesCategoryApiModel::class -> {
            (this as List<ClothesCategoryApiModel>).map() as List<C>
        }
        ClothesTypeApiModel::class -> {
            (this as List<ClothesTypeApiModel>).map() as List<C>
        }
        ClothesStyleApiModel::class -> {
            (this as List<ClothesStyleApiModel>).map() as List<C>
        }
        ClothesBrandApiModel::class -> {
            (this as List<ClothesBrandApiModel>).map() as List<C>
        }
        ClothesColorApiModel::class -> {
            (this as List<ClothesColorApiModel>).map() as List<C>
        }
        AddressApiModel::class -> {
            (this as List<AddressApiModel>).map() as List<C>
        }
        UserApiModel::class -> {
            (this as List<UserApiModel>).map() as List<C>
        }
        FollowerApiModel::class -> {
            (this as List<FollowerApiModel>).map() as List<C>
        }
        OutfitApiModel::class -> {
            (this as List<OutfitApiModel>).map() as List<C>
        }
        PostApiModel::class -> {
            (this as List<PostApiModel>).map() as List<C>
        }
        WardrobeApiModel::class -> {
            (this as List<WardrobeApiModel>).map() as List<C>
        }
        CommentApiModel::class -> {
            (this as List<CommentApiModel>).map() as List<C>
        }
        OrderApiModel::class -> {
            (this as List<OrderApiModel>).map() as List<C>
        }
        ReferralApiModel::class -> {
            (this as List<ReferralApiModel>).map() as List<C>
        }
        else -> emptyList()
    }
}