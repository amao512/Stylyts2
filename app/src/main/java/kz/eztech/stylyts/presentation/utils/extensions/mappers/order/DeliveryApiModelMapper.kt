package kz.eztech.stylyts.presentation.utils.extensions.mappers.order

import kz.eztech.stylyts.data.api.models.order.DeliveryApiModel
import kz.eztech.stylyts.domain.models.order.DeliveryModel
import kz.eztech.stylyts.presentation.utils.EMPTY_STRING

fun DeliveryApiModel?.map(): DeliveryModel {
    return DeliveryModel(
        city = this?.city ?: EMPTY_STRING,
        street = this?.street ?: EMPTY_STRING,
        house = this?.house ?: EMPTY_STRING,
        apartment = this?.apartment ?: EMPTY_STRING,
        deliveryType = this?.deliveryType ?: EMPTY_STRING,
        deliveryStatus = this?.deliveryStatus ?: EMPTY_STRING,
    )
}