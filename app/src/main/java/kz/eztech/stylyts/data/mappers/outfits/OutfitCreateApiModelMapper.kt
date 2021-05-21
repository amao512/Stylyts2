package kz.eztech.stylyts.data.mappers.outfits

import kz.eztech.stylyts.data.api.models.outfits.OutfitCreateApiModel
import kz.eztech.stylyts.domain.models.outfits.OutfitCreateModel
import kz.eztech.stylyts.presentation.utils.EMPTY_STRING
import javax.inject.Inject

class OutfitCreateApiModelMapper @Inject constructor() {

    fun map(data: OutfitCreateApiModel?): OutfitCreateModel {
        return OutfitCreateModel(
            id = data?.id ?: 0,
            title = data?.title ?: EMPTY_STRING,
            clothesIdList = data?.clothes ?: emptyList(),
            clothes = emptyList(),
            itemLocation = emptyList(),
            style = data?.style ?: 0,
            authorId = data?.author?.id ?: 0,
            text = data?.text ?: EMPTY_STRING
        )
    }
}