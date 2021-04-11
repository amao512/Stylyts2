package kz.eztech.stylyts.data.mappers

import kz.eztech.stylyts.data.api.models.ClothesBrandApiModel
import kz.eztech.stylyts.domain.models.clothes.ClothesBrandModel
import kz.eztech.stylyts.presentation.utils.EMPTY_STRING
import javax.inject.Inject

class ClothesBrandApiModelMapper @Inject constructor() {

    fun map(data: ClothesBrandApiModel?): ClothesBrandModel {
        return ClothesBrandModel(
            id = data?.id ?: 0,
            title = data?.title ?: EMPTY_STRING,
            website = data?.website ?: EMPTY_STRING,
            logo = data?.logo ?: EMPTY_STRING,
            createdAt = data?.createdAt ?: EMPTY_STRING,
            modifiedAt = data?.modifiedAt ?: EMPTY_STRING
        )
    }

    fun map(data: List<ClothesBrandApiModel>?): List<ClothesBrandModel> {
        data ?: return emptyList()

        return data.map {
            ClothesBrandModel(
                id = it.id ?: 0,
                title = it.title ?: EMPTY_STRING,
                website = it.website ?: EMPTY_STRING,
                logo = it.logo ?: EMPTY_STRING,
                createdAt = it.createdAt ?: EMPTY_STRING,
                modifiedAt = it.modifiedAt ?: EMPTY_STRING
            )
        }
    }
}