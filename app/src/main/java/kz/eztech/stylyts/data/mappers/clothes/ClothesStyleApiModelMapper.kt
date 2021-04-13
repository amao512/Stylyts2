package kz.eztech.stylyts.data.mappers.clothes

import kz.eztech.stylyts.data.api.models.clothes.ClothesStyleApiModel
import kz.eztech.stylyts.domain.models.clothes.ClothesStyleModel
import kz.eztech.stylyts.presentation.utils.EMPTY_STRING
import javax.inject.Inject

class ClothesStyleApiModelMapper @Inject constructor() {

    fun map(data: ClothesStyleApiModel?): ClothesStyleModel {
        return ClothesStyleModel(
            id = data?.id ?: 0,
            title = data?.title ?: EMPTY_STRING
        )
    }

    fun map(data: List<ClothesStyleApiModel>?): List<ClothesStyleModel> {
        data ?: return emptyList()

        return data.map {
            ClothesStyleModel(
                id = it.id ?: 0,
                title = it.title ?: EMPTY_STRING
            )
        }
    }
}