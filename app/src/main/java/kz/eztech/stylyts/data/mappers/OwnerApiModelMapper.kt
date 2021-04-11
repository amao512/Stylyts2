package kz.eztech.stylyts.data.mappers

import kz.eztech.stylyts.data.api.models.OwnerApiModel
import kz.eztech.stylyts.domain.models.OwnerModel
import kz.eztech.stylyts.presentation.utils.EMPTY_STRING
import javax.inject.Inject

/**
 * Created by Asylzhan Seytbek on 11.04.2021.
 */
class OwnerApiModelMapper @Inject constructor() {

    fun map(data: OwnerApiModel?): OwnerModel {
        return OwnerModel(
            id = data?.id ?: 0,
            username = data?.username ?: EMPTY_STRING
        )
    }
}