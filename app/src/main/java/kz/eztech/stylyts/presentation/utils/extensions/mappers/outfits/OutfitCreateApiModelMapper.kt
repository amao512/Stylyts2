package kz.eztech.stylyts.presentation.utils.extensions.mappers.outfits

import kz.eztech.stylyts.data.api.models.outfits.OutfitCreateApiModel
import kz.eztech.stylyts.domain.models.outfits.OutfitCreateModel
import kz.eztech.stylyts.presentation.utils.EMPTY_STRING

fun OutfitCreateApiModel?.map(): OutfitCreateModel {
    return OutfitCreateModel(
        id = this?.id ?: 0,
        title = this?.title ?: EMPTY_STRING,
        clothesIdList = this?.clothes ?: emptyList(),
        clothes = emptyList(),
        itemLocation = emptyList(),
        style = this?.style ?: 0,
        authorId = this?.author?.id ?: 0,
        text = this?.text ?: EMPTY_STRING
    )
}