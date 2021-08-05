package kz.eztech.stylyts.presentation.utils.mappers.clothes

import kz.eztech.stylyts.data.api.models.clothes.ClothesBrandApiModel
import kz.eztech.stylyts.domain.models.clothes.ClothesBrandModel

fun ClothesBrandApiModel?.map(): ClothesBrandModel {
    return ClothesBrandModel(
        id = this?.id ?: 0,
        title = this?.title.orEmpty(),
        website = this?.website.orEmpty(),
        logo = this?.logo.orEmpty(),
        createdAt = this?.createdAt.orEmpty(),
        modifiedAt = this?.modifiedAt.orEmpty()
    )
}

fun List<ClothesBrandApiModel>?.map(): List<ClothesBrandModel> {
    this ?: return emptyList()

    return this.map {
        ClothesBrandModel(
            id = it.id ?: 0,
            title = it.title.orEmpty(),
            website = it.website.orEmpty(),
            logo = it.logo.orEmpty(),
            createdAt = it.createdAt.orEmpty(),
            modifiedAt = it.modifiedAt.orEmpty()
        )
    }
}