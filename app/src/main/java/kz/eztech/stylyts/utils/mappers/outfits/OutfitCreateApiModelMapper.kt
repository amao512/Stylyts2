package kz.eztech.stylyts.utils.mappers.outfits

import kz.eztech.stylyts.data.api.models.outfits.OutfitCreateApiModel
import kz.eztech.stylyts.domain.models.outfits.OutfitCreateModel

fun OutfitCreateApiModel?.map(): OutfitCreateModel {
    return OutfitCreateModel(
        id = this?.id ?: 0,
        title = this?.title.orEmpty(),
        clothesIdList = this?.clothes ?: emptyList(),
        clothes = emptyList(),
        itemLocation = emptyList(),
        style = this?.style ?: 0,
        authorId = this?.author?.id ?: 0,
        text = this?.text.orEmpty()
    )
}