package kz.eztech.stylyts.data.mappers.wardrobe

import kz.eztech.stylyts.data.api.models.wardrobe.WardrobeApiModel
import kz.eztech.stylyts.domain.models.wardrobe.WardrobeModel
import kz.eztech.stylyts.presentation.utils.EMPTY_STRING
import javax.inject.Inject

class WardrobeApiModelMapper @Inject constructor() {

    fun map(data: List<WardrobeApiModel>?): List<WardrobeModel> {
        data ?: return emptyList()

        return data.map {
            WardrobeModel(
                id = it.id ?: 0,
                createdAt = it.createdAt ?: EMPTY_STRING,
                modifiedAt = it.modifiedAt ?: EMPTY_STRING,
                user = it.user ?: 0,
                clothes = it.clothes ?: 0
            )
        }
    }

    fun map(data: WardrobeApiModel?): WardrobeModel {
        return WardrobeModel(
            id = data?.id ?: 0,
            createdAt = data?.createdAt ?: EMPTY_STRING,
            modifiedAt = data?.modifiedAt ?: EMPTY_STRING,
            user = data?.user ?: 0,
            clothes = data?.clothes ?: 0
        )
    }
}