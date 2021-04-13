package kz.eztech.stylyts.data.mappers.outfits

import kz.eztech.stylyts.data.api.models.outfits.ClothesLocationApiModel
import kz.eztech.stylyts.domain.models.outfits.ClothesLocationModel
import javax.inject.Inject

class ClothesLocationApiModelMapper @Inject constructor() {

    fun map(data: ClothesLocationApiModel?): ClothesLocationModel {
        return ClothesLocationModel(
            clothesId = data?.clothesId ?: 0,
            pointX = data?.pointX ?: 0.0,
            pointY = data?.pointY ?: 0.0,
            width = data?.width ?: 0.0,
            height = data?.height ?: 0.0,
            degree = data?.degree ?: 0.0
        )
    }

    fun map(data: List<ClothesLocationApiModel>?): List<ClothesLocationModel> {
        data ?: return emptyList()

        return data.map {
            ClothesLocationModel(
                clothesId = it.clothesId ?: 0,
                pointX = it.pointX ?: 0.0,
                pointY = it.pointY ?: 0.0,
                width = it.width ?: 0.0,
                height = it.height ?: 0.0,
                degree = it.degree ?: 0.0
            )
        }
    }
}