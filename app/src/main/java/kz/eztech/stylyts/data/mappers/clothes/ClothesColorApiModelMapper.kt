package kz.eztech.stylyts.data.mappers.clothes

import kz.eztech.stylyts.data.api.models.clothes.ClothesColorApiModel
import kz.eztech.stylyts.domain.models.clothes.ClothesColorModel
import kz.eztech.stylyts.presentation.utils.EMPTY_STRING
import javax.inject.Inject

class ClothesColorApiModelMapper @Inject constructor() {

    fun map(data: List<ClothesColorApiModel>?): List<ClothesColorModel> {
        data ?: return emptyList()

        return data.map {
            ClothesColorModel(
                id = it.id ?: 0,
                title = it.title ?: EMPTY_STRING,
                color = it.color ?: EMPTY_STRING
            )
        }
    }
}