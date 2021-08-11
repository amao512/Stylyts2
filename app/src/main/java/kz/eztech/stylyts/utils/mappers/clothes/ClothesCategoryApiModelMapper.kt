package kz.eztech.stylyts.utils.mappers.clothes

import kz.eztech.stylyts.data.api.models.clothes.ClothesCategoryApiModel
import kz.eztech.stylyts.domain.models.clothes.ClothesCategoryModel

fun ClothesCategoryApiModel?.map(): ClothesCategoryModel {
    return ClothesCategoryModel(
        id = this?.id ?: 0,
        clothesType = this?.clothesType.map(),
        title = this?.title.orEmpty(),
        bodyPart = this?.bodyPart ?: 0
    )
}

fun List<ClothesCategoryApiModel>?.map(): List<ClothesCategoryModel> {
    this ?: return emptyList()

    return this.map {
        ClothesCategoryModel(
            id = it.id ?: 0,
            clothesType = it.clothesType.map(),
            title = it.title.orEmpty(),
            bodyPart = it.bodyPart ?: 0
        )
    }
}