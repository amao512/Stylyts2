package kz.eztech.stylyts.presentation.utils.extensions.mappers.clothes

import kz.eztech.stylyts.data.api.models.clothes.ClothesStyleApiModel
import kz.eztech.stylyts.domain.models.clothes.ClothesStyleModel
import kz.eztech.stylyts.presentation.utils.EMPTY_STRING

fun ClothesStyleApiModel?.map(): ClothesStyleModel {
    return ClothesStyleModel(
        id = this?.id ?: 0,
        title = this?.title ?: EMPTY_STRING
    )
}

fun List<ClothesStyleApiModel>?.map(): List<ClothesStyleModel> {
    this ?: return emptyList()

    return this.map {
        ClothesStyleModel(
            id = it.id ?: 0,
            title = it.title ?: EMPTY_STRING
        )
    }
}