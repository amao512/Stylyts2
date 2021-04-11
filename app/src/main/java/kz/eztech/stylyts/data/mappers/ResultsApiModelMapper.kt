package kz.eztech.stylyts.data.mappers

import kz.eztech.stylyts.data.api.models.*
import kz.eztech.stylyts.domain.models.ResultsModel
import kz.eztech.stylyts.domain.models.clothes.ClothesBrandModel
import kz.eztech.stylyts.domain.models.clothes.ClothesCategoryModel
import kz.eztech.stylyts.domain.models.clothes.ClothesModel
import kz.eztech.stylyts.domain.models.clothes.ClothesTypeModel
import javax.inject.Inject

class ResultsApiModelMapper @Inject constructor(
    private val clothesApiModelMapper: ClothesApiModelMapper,
    private val clothesCategoryApiModelMapper: ClothesCategoryApiModelMapper,
    private val clothesTypeApiModelMapper: ClothesTypeApiModelMapper,
    private val clothesBrandApiModelMapper: ClothesBrandApiModelMapper
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

    fun mapBrandResults(data: ResultsApiModel<ClothesBrandApiModel>?): ResultsModel<ClothesBrandModel> {
        return ResultsModel(
            page = data?.page ?: 0,
            totalPages = data?.totalPages ?: 0,
            pageSize = data?.pageSize ?: 0,
            totalCount = data?.totalCount ?: 0,
            results = clothesBrandApiModelMapper.map(data?.results)
        )
    }
}