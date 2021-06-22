package kz.eztech.stylyts.presentation.utils.extensions.mappers.clothes

import kz.eztech.stylyts.data.api.models.clothes.ClothesBrandApiModel
import kz.eztech.stylyts.domain.models.clothes.ClothesBrandModel
import kz.eztech.stylyts.presentation.utils.EMPTY_STRING

fun ClothesBrandApiModel?.map(): ClothesBrandModel {
    return ClothesBrandModel(
        id = this?.id ?: 0,
        title = this?.title ?: EMPTY_STRING,
        website = this?.website ?: EMPTY_STRING,
        logo = this?.logo ?: EMPTY_STRING,
        createdAt = this?.createdAt ?: EMPTY_STRING,
        modifiedAt = this?.modifiedAt ?: EMPTY_STRING
    )
}

fun List<ClothesBrandApiModel>?.map(): List<ClothesBrandModel> {
    this ?: return emptyList()

    return this.map {
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