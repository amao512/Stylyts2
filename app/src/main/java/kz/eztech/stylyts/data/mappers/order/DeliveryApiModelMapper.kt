package kz.eztech.stylyts.data.mappers.order

import kz.eztech.stylyts.data.api.models.order.DeliveryApiModel
import kz.eztech.stylyts.domain.models.order.DeliveryModel
import kz.eztech.stylyts.presentation.utils.EMPTY_STRING
import javax.inject.Inject

class DeliveryApiModelMapper @Inject constructor() {

    fun map(data: DeliveryApiModel?): DeliveryModel {
        return DeliveryModel(
            city = data?.city ?: EMPTY_STRING,
            street = data?.street ?: EMPTY_STRING,
            house = data?.house ?: EMPTY_STRING,
            apartment = data?.apartment ?: EMPTY_STRING,
            deliveryType = data?.deliveryType ?: EMPTY_STRING,
            deliveryStatus =  EMPTY_STRING,
        )
    }
}