package kz.eztech.stylyts.presentation.utils.extensions.mappers.clothes

import kz.eztech.stylyts.data.api.models.clothes.ClothesColorApiModel
import kz.eztech.stylyts.domain.models.clothes.ClothesColorModel
import kz.eztech.stylyts.presentation.utils.EMPTY_STRING

fun List<ClothesColorApiModel>?.map(): List<ClothesColorModel> {
    this ?: return emptyList()

    return this.map {
        ClothesColorModel(
            id = it.id ?: 0,
            title = it.title ?: EMPTY_STRING,
            color = it.color ?: EMPTY_STRING
        )
    }
}