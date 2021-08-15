package kz.eztech.stylyts.utils.mappers.clothes

import kz.eztech.stylyts.global.data.models.clothes.ClothesSizeApiModel
import kz.eztech.stylyts.global.domain.models.clothes.ClothesSizeModel

fun List<ClothesSizeApiModel>?.map(): List<ClothesSizeModel> {
    this ?: return emptyList()

    return this.map {
        ClothesSizeModel(
            size = it.size.orEmpty(),
            count = it.count ?: 0
        )
    }
}