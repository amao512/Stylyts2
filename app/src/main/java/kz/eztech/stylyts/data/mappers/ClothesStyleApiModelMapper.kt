package kz.eztech.stylyts.data.mappers

import kz.eztech.stylyts.data.api.models.ClothesStyleApiModel
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
}