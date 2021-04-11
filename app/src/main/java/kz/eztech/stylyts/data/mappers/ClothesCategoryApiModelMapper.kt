package kz.eztech.stylyts.data.mappers

import kz.eztech.stylyts.data.api.models.ClothesCategoryApiModel
import kz.eztech.stylyts.domain.models.clothes.ClothesCategoryModel
import kz.eztech.stylyts.presentation.utils.EMPTY_STRING
import javax.inject.Inject

class ClothesCategoryApiModelMapper @Inject constructor(
    private val clothesTypeApiModelMapper: ClothesTypeApiModelMapper
) {

    fun map(data: ClothesCategoryApiModel?): ClothesCategoryModel {
        return ClothesCategoryModel(
            id = data?.id ?: 0,
            clothesType = clothesTypeApiModelMapper.map(data?.clothesType),
            title = data?.title ?: EMPTY_STRING,
            bodyPart = data?.bodyPart ?: 0
        )
    }

    fun map(data: List<ClothesCategoryApiModel>?): List<ClothesCategoryModel> {
        data ?: return emptyList()

        return data.map {
            ClothesCategoryModel(
                id = it.id ?: 0,
                clothesType = clothesTypeApiModelMapper.map(it.clothesType),
                title = it.title ?: EMPTY_STRING,
                bodyPart = it.bodyPart ?: 0
            )
        }
    }
}