package kz.eztech.stylyts.presentation.utils.extensions.mappers.clothes

import kz.eztech.stylyts.data.api.models.clothes.ClothesCategoryApiModel
import kz.eztech.stylyts.domain.models.clothes.ClothesCategoryModel
import kz.eztech.stylyts.presentation.utils.EMPTY_STRING

fun ClothesCategoryApiModel?.map(): ClothesCategoryModel {
    return ClothesCategoryModel(
        id = this?.id ?: 0,
        clothesType = this?.clothesType.map(),
        title = this?.title ?: EMPTY_STRING,
        bodyPart = this?.bodyPart ?: 0
    )
}

fun List<ClothesCategoryApiModel>?.map(): List<ClothesCategoryModel> {
    this ?: return emptyList()

    return this.map {
        ClothesCategoryModel(
            id = it.id ?: 0,
            clothesType = it.clothesType.map(),
            title = it.title ?: EMPTY_STRING,
            bodyPart = it.bodyPart ?: 0
        )
    }
}