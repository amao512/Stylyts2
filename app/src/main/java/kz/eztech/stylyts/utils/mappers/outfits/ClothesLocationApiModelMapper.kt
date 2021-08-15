package kz.eztech.stylyts.utils.mappers.outfits

import kz.eztech.stylyts.global.domain.models.outfits.ClothesLocationApiModel
import kz.eztech.stylyts.global.domain.models.outfits.ItemLocationModel

fun ClothesLocationApiModel?.map(): ItemLocationModel {
    return ItemLocationModel(
        id = this?.clothesId ?: 0,
        pointX = this?.pointX ?: 0.0,
        pointY = this?.pointY ?: 0.0,
        width = this?.width ?: 0.0,
        height = this?.height ?: 0.0,
        degree = this?.degree ?: 0.0
    )
}

fun List<ClothesLocationApiModel>?.map(): List<ItemLocationModel> {
    this ?: return emptyList()

    return this.map {
        ItemLocationModel(
            id = it.clothesId ?: 0,
            pointX = it.pointX ?: 0.0,
            pointY = it.pointY ?: 0.0,
            width = it.width ?: 0.0,
            height = it.height ?: 0.0,
            degree = it.degree ?: 0.0
        )
    }
}