package kz.eztech.stylyts.presentation.utils.mappers.clothes

import kz.eztech.stylyts.data.api.models.clothes.ClothesStyleApiModel
import kz.eztech.stylyts.domain.models.clothes.ClothesStyleModel

fun ClothesStyleApiModel?.map(): ClothesStyleModel {
    return ClothesStyleModel(
        id = this?.id ?: 0,
        title = this?.title.orEmpty()
    )
}

fun List<ClothesStyleApiModel>?.map(): List<ClothesStyleModel> {
    this ?: return emptyList()

    return this.map {
        ClothesStyleModel(
            id = it.id ?: 0,
            title = it.title.orEmpty()
        )
    }
}