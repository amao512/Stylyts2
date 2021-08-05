package kz.eztech.stylyts.presentation.utils.mappers.clothes

import kz.eztech.stylyts.data.api.models.clothes.ClothesColorApiModel
import kz.eztech.stylyts.domain.models.clothes.ClothesColorModel

fun List<ClothesColorApiModel>?.map(): List<ClothesColorModel> {
    this ?: return emptyList()

    return this.map {
        ClothesColorModel(
            id = it.id ?: 0,
            title = it.title.orEmpty(),
            color = it.color.orEmpty()
        )
    }
}