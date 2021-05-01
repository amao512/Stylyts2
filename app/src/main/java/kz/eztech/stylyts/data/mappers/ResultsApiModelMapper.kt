package kz.eztech.stylyts.data.mappers

import kz.eztech.stylyts.data.api.models.ResultsApiModel
import kz.eztech.stylyts.data.api.models.address.AddressApiModel
import kz.eztech.stylyts.data.api.models.clothes.*
import kz.eztech.stylyts.data.api.models.comments.CommentApiModel
import kz.eztech.stylyts.data.api.models.outfits.OutfitApiModel
import kz.eztech.stylyts.data.api.models.posts.PostApiModel
import kz.eztech.stylyts.data.api.models.user.FollowerApiModel
import kz.eztech.stylyts.data.api.models.user.UserApiModel
import kz.eztech.stylyts.data.api.models.wardrobe.WardrobeApiModel
import kz.eztech.stylyts.data.mappers.address.AddressApiModelMapper
import kz.eztech.stylyts.data.mappers.clothes.*
import kz.eztech.stylyts.data.mappers.comments.CommentApiModelMapper
import kz.eztech.stylyts.data.mappers.outfits.OutfitApiModelMapper
import kz.eztech.stylyts.data.mappers.posts.PostApiModelMapper
import kz.eztech.stylyts.data.mappers.user.FollowerApiModelMapper
import kz.eztech.stylyts.data.mappers.user.UserApiModelMapper
import kz.eztech.stylyts.data.mappers.wardrobe.WardrobeApiModelMapper
import kz.eztech.stylyts.domain.models.ResultsModel
import javax.inject.Inject

/**
 * Created by Asylzhan Seytbek on 11.04.2021.
 */
class ResultsApiModelMapper @Inject constructor(
    val clothesApiModelMapper: ClothesApiModelMapper,
    val clothesCategoryApiModelMapper: ClothesCategoryApiModelMapper,
    val clothesTypeApiModelMapper: ClothesTypeApiModelMapper,
    val clothesStyleApiModelMapper: ClothesStyleApiModelMapper,
    val clothesBrandApiModelMapper: ClothesBrandApiModelMapper,
    val addressApiModelMapper: AddressApiModelMapper,
    val userApiModelMapper: UserApiModelMapper,
    val followerApiModelMapper: FollowerApiModelMapper,
    val outfitApiModelMapper: OutfitApiModelMapper,
    val postApiModelMapper: PostApiModelMapper,
    val wardrobeApiModelMapper: WardrobeApiModelMapper,
    val commentApiModelMapper: CommentApiModelMapper
) {

    inline fun <reified T, C> map(data: ResultsApiModel<T>?): ResultsModel<C> {
        return ResultsModel(
            page = data?.page ?: 0,
            totalPages = data?.totalPages ?: 0,
            pageSize = data?.pageSize ?: 0,
            totalCount = data?.totalCount ?: 0,
            results = mapResults(data?.results)
        )
    }

    @Suppress("UNCHECKED_CAST")
    inline fun <reified T, C> mapResults(results: List<T>?): List<C> {
        return when (T::class) {
            ClothesApiModel::class -> {
                clothesApiModelMapper.map(results as List<ClothesApiModel>) as List<C>
            }
            ClothesCategoryApiModel::class -> {
                clothesCategoryApiModelMapper.map(results as List<ClothesCategoryApiModel>) as List<C>
            }
            ClothesTypeApiModel::class -> {
                clothesTypeApiModelMapper.map(results as List<ClothesTypeApiModel>) as List<C>
            }
            ClothesStyleApiModel::class -> {
                clothesStyleApiModelMapper.map(results as List<ClothesStyleApiModel>) as List<C>
            }
            ClothesBrandApiModel::class -> {
                clothesBrandApiModelMapper.map(results as List<ClothesBrandApiModel>) as List<C>
            }
            AddressApiModel::class -> {
                addressApiModelMapper.map(results as List<AddressApiModel>) as List<C>
            }
            UserApiModel::class -> {
                userApiModelMapper.map(results as List<UserApiModel>) as List<C>
            }
            FollowerApiModel::class -> {
                followerApiModelMapper.map(results as List<FollowerApiModel>) as List<C>
            }
            OutfitApiModel::class -> {
                outfitApiModelMapper.map(results as List<OutfitApiModel>) as List<C>
            }
            PostApiModel::class -> {
                postApiModelMapper.map(results as List<PostApiModel>) as List<C>
            }
            WardrobeApiModel::class -> {
                wardrobeApiModelMapper.map(results as List<WardrobeApiModel>) as List<C>
            }
            CommentApiModel::class -> {
                commentApiModelMapper.map(results as List<CommentApiModel>) as List<C>
            }
            else -> emptyList()
        }
    }
}