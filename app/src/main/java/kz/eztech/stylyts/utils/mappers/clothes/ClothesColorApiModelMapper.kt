package kz.eztech.stylyts.utils.mappers.clothes

import kz.eztech.stylyts.global.data.models.clothes.ClothesColorApiModel
import kz.eztech.stylyts.global.domain.models.clothes.ClothesColorModel

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