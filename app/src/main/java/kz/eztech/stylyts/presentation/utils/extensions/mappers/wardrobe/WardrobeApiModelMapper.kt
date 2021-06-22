package kz.eztech.stylyts.presentation.utils.extensions.mappers.wardrobe

import kz.eztech.stylyts.data.api.models.wardrobe.WardrobeApiModel
import kz.eztech.stylyts.domain.models.wardrobe.WardrobeModel
import kz.eztech.stylyts.presentation.utils.EMPTY_STRING

fun List<WardrobeApiModel>?.map(): List<WardrobeModel> {
    this ?: return emptyList()

    return this.map {
        WardrobeModel(
            id = it.id ?: 0,
            createdAt = it.createdAt ?: EMPTY_STRING,
            modifiedAt = it.modifiedAt ?: EMPTY_STRING,
            user = it.user ?: 0,
            clothes = it.clothes ?: 0
        )
    }
}

fun WardrobeApiModel?.map(): WardrobeModel {
    return WardrobeModel(
        id = this?.id ?: 0,
        createdAt = this?.createdAt ?: EMPTY_STRING,
        modifiedAt = this?.modifiedAt ?: EMPTY_STRING,
        user = this?.user ?: 0,
        clothes = this?.clothes ?: 0
    )
}