package kz.eztech.stylyts.data.mappers.outfits

import kz.eztech.stylyts.data.api.models.outfits.ClothesLocationApiModel
import kz.eztech.stylyts.domain.models.outfits.ItemLocationModel
import javax.inject.Inject

class ClothesLocationApiModelMapper @Inject constructor() {

    fun map(data: ClothesLocationApiModel?): ItemLocationModel {
        return ItemLocationModel(
            id = data?.clothesId ?: 0,
            pointX = data?.pointX ?: 0.0,
            pointY = data?.pointY ?: 0.0,
            width = data?.width ?: 0.0,
            height = data?.height ?: 0.0,
            degree = data?.degree ?: 0.0
        )
    }

    fun map(data: List<ClothesLocationApiModel>?): List<ItemLocationModel> {
        data ?: return emptyList()

        return data.map {
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
}