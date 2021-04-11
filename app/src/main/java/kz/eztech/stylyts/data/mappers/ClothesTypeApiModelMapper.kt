package kz.eztech.stylyts.data.mappers

import kz.eztech.stylyts.data.api.models.ClothesTypeApiModel
import kz.eztech.stylyts.domain.models.clothes.ClothesTypeModel
import kz.eztech.stylyts.presentation.utils.EMPTY_STRING
import javax.inject.Inject

class ClothesTypeApiModelMapper @Inject constructor() {

    fun map(data: ClothesTypeApiModel?): ClothesTypeModel {
        return ClothesTypeModel(
            id = data?.id ?: 0,
            title = data?.title ?: EMPTY_STRING,
            menCoverPhoto = data?.menCoverPhoto ?: EMPTY_STRING,
            womenCoverPhoto = data?.womenCoverPhoto ?: EMPTY_STRING
        )
    }

    fun map(data: List<ClothesTypeApiModel>?): List<ClothesTypeModel> {
        data ?: return emptyList()

        return data.map {
            ClothesTypeModel(
                id = it.id ?: 0,
                title = it.title ?: EMPTY_STRING,
                menCoverPhoto = it.menCoverPhoto ?: EMPTY_STRING,
                womenCoverPhoto = it.womenCoverPhoto ?: EMPTY_STRING
            )
        }
    }
}