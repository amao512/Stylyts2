package kz.eztech.stylyts.utils.mappers.wardrobe

import kz.eztech.stylyts.profile.data.models.WardrobeApiModel
import kz.eztech.stylyts.profile.domain.models.wardrobe.WardrobeModel

fun List<WardrobeApiModel>?.map(): List<WardrobeModel> {
    this ?: return emptyList()

    return this.map {
        WardrobeModel(
            id = it.id ?: 0,
            createdAt = it.createdAt.orEmpty(),
            modifiedAt = it.modifiedAt.orEmpty(),
            user = it.user ?: 0,
            clothes = it.clothes ?: 0
        )
    }
}

fun WardrobeApiModel?.map(): WardrobeModel {
    return WardrobeModel(
        id = this?.id ?: 0,
        createdAt = this?.createdAt.orEmpty(),
        modifiedAt = this?.modifiedAt.orEmpty(),
        user = this?.user ?: 0,
        clothes = this?.clothes ?: 0
    )
}