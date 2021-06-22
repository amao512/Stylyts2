package kz.eztech.stylyts.presentation.utils.extensions.mappers.clothes

import kz.eztech.stylyts.data.api.models.clothes.ClothesSizeApiModel
import kz.eztech.stylyts.domain.models.clothes.ClothesSizeModel
import kz.eztech.stylyts.presentation.utils.EMPTY_STRING

fun List<ClothesSizeApiModel>?.map(): List<ClothesSizeModel> {
    this ?: return emptyList()

    return this.map {
        ClothesSizeModel(
            size = it.size ?: EMPTY_STRING,
            count = it.count ?: 0
        )
    }
}