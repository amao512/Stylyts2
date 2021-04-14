package kz.eztech.stylyts.data.mappers.clothes

import kz.eztech.stylyts.data.api.models.clothes.ClothesSizeApiModel
import kz.eztech.stylyts.domain.models.clothes.ClothesSizeModel
import kz.eztech.stylyts.presentation.utils.EMPTY_STRING
import javax.inject.Inject

class ClothesSizeApiModelMapper @Inject constructor() {

    fun map(data: List<ClothesSizeApiModel>?): List<ClothesSizeModel> {
        data ?: return emptyList()

        return data.map {
            ClothesSizeModel(
                size = it.size ?: EMPTY_STRING,
                count = it.count ?: 0
            )
        }
    }
}