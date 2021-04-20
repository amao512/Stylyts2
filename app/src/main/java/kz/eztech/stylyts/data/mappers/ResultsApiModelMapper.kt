package kz.eztech.stylyts.data.mappers

import kz.eztech.stylyts.data.api.models.ResultsApiModel
import kz.eztech.stylyts.data.api.models.address.AddressApiModel
import kz.eztech.stylyts.data.api.models.clothes.*
import kz.eztech.stylyts.data.api.models.outfits.OutfitApiModel
import kz.eztech.stylyts.data.api.models.posts.PostApiModel
import kz.eztech.stylyts.data.api.models.user.FollowerApiModel
import kz.eztech.stylyts.data.api.models.user.UserApiModel
import kz.eztech.stylyts.data.mappers.address.AddressApiModelMapper
import kz.eztech.stylyts.data.mappers.clothes.*
import kz.eztech.stylyts.data.mappers.outfits.OutfitApiModelMapper
import kz.eztech.stylyts.data.mappers.posts.PostApiModelMapper
import kz.eztech.stylyts.data.mappers.user.FollowerApiModelMapper
import kz.eztech.stylyts.data.mappers.user.UserApiModelMapper
import kz.eztech.stylyts.domain.models.ResultsModel
import kz.eztech.stylyts.domain.models.address.AddressModel
import kz.eztech.stylyts.domain.models.clothes.*
import kz.eztech.stylyts.domain.models.outfits.OutfitModel
import kz.eztech.stylyts.domain.models.posts.PostModel
import kz.eztech.stylyts.domain.models.user.FollowerModel
import kz.eztech.stylyts.domain.models.user.UserModel
import javax.inject.Inject

/**
 * Created by Asylzhan Seytbek on 11.04.2021.
 */
class ResultsApiModelMapper @Inject constructor(
    private val clothesApiModelMapper: ClothesApiModelMapper,
    private val clothesCategoryApiModelMapper: ClothesCategoryApiModelMapper,
    private val clothesTypeApiModelMapper: ClothesTypeApiModelMapper,
    private val clothesStyleApiModelMapper: ClothesStyleApiModelMapper,
    private val clothesBrandApiModelMapper: ClothesBrandApiModelMapper,
    private val addressApiModelMapper: AddressApiModelMapper,
    private val userApiModelMapper: UserApiModelMapper,
    private val followerApiModelMapper: FollowerApiModelMapper,
    private val outfitApiModelMapper: OutfitApiModelMapper,
    private val postApiModelMapper: PostApiModelMapper
) {

    fun mapClothesResults(data: ResultsApiModel<ClothesApiModel>?): ResultsModel<ClothesModel> {
        return ResultsModel(
            page = data?.page ?: 0,
            totalPages = data?.totalPages ?: 0,
            pageSize = data?.pageSize ?: 0,
            totalCount = data?.totalCount ?: 0,
            results = clothesApiModelMapper.map(data?.results)
        )
    }

    fun mapCategoryResults(data: ResultsApiModel<ClothesCategoryApiModel>?): ResultsModel<ClothesCategoryModel> {
        return ResultsModel(
            page = data?.page ?: 0,
            totalPages = data?.totalPages ?: 0,
            pageSize = data?.pageSize ?: 0,
            totalCount = data?.totalCount ?: 0,
            results = clothesCategoryApiModelMapper.map(data?.results)
        )
    }

    fun mapTypeResults(data: ResultsApiModel<ClothesTypeApiModel>?): ResultsModel<ClothesTypeModel> {
        return ResultsModel(
            page = data?.page ?: 0,
            totalPages = data?.totalPages ?: 0,
            pageSize = data?.pageSize ?: 0,
            totalCount = data?.totalCount ?: 0,
            results = clothesTypeApiModelMapper.map(data?.results)
        )
    }

    fun mapStyleResults(data: ResultsApiModel<ClothesStyleApiModel>?): ResultsModel<ClothesStyleModel> {
        return ResultsModel(
            page = data?.page ?: 0,
            totalPages = data?.totalPages ?: 0,
            pageSize = data?.pageSize ?: 0,
            totalCount = data?.totalCount ?: 0,
            results = clothesStyleApiModelMapper.map(data?.results)
        )
    }

    fun mapBrandResults(data: ResultsApiModel<ClothesBrandApiModel>?): ResultsModel<ClothesBrandModel> {
        return ResultsModel(
            page = data?.page ?: 0,
            totalPages = data?.totalPages ?: 0,
            pageSize = data?.pageSize ?: 0,
            totalCount = data?.totalCount ?: 0,
            results = clothesBrandApiModelMapper.map(data?.results)
        )
    }

    fun mapAddressResults(data: ResultsApiModel<AddressApiModel>?): ResultsModel<AddressModel> {
        return ResultsModel(
            page = data?.page ?: 0,
            totalPages = data?.totalPages ?: 0,
            pageSize = data?.pageSize ?: 0,
            totalCount = data?.totalCount ?: 0,
            results = addressApiModelMapper.map(data?.results)
        )
    }

    fun mapUserResults(data: ResultsApiModel<UserApiModel>?): ResultsModel<UserModel> {
        return ResultsModel(
            page = data?.page ?: 0,
            totalPages = data?.totalPages ?: 0,
            pageSize = data?.pageSize ?: 0,
            totalCount = data?.totalCount ?: 0,
            results = userApiModelMapper.map(data?.results)
        )
    }

    fun mapFollowerResults(data: ResultsApiModel<FollowerApiModel>?): ResultsModel<FollowerModel> {
        return ResultsModel(
            page = data?.page ?: 0,
            totalPages = data?.totalPages ?: 0,
            pageSize = data?.pageSize ?: 0,
            totalCount = data?.totalCount ?: 0,
            results = followerApiModelMapper.map(data?.results)
        )
    }

    fun mapOutfitResults(data: ResultsApiModel<OutfitApiModel>?): ResultsModel<OutfitModel> {
        return ResultsModel(
            page = data?.page ?: 0,
            totalPages = data?.totalPages ?: 0,
            pageSize = data?.pageSize ?: 0,
            totalCount = data?.totalCount ?: 0,
            results = outfitApiModelMapper.map(data?.results)
        )
    }

    fun mapPostResults(data: ResultsApiModel<PostApiModel>?): ResultsModel<PostModel> {
        return ResultsModel(
            page = data?.page ?: 0,
            totalPages = data?.totalPages ?: 0,
            pageSize = data?.pageSize ?: 0,
            totalCount = data?.totalCount ?: 0,
            results = postApiModelMapper.map(data?.results)
        )
    }
}