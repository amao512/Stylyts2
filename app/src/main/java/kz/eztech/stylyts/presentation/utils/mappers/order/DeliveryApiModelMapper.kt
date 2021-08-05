package kz.eztech.stylyts.presentation.utils.mappers.order

import kz.eztech.stylyts.data.api.models.order.DeliveryApiModel
import kz.eztech.stylyts.domain.models.order.DeliveryModel

fun DeliveryApiModel?.map(): DeliveryModel {
    return DeliveryModel(
        city = this?.city.orEmpty(),
        street = this?.street.orEmpty(),
        house = this?.house.orEmpty(),
        apartment = this?.apartment.orEmpty(),
        deliveryType = this?.deliveryType.orEmpty(),
        deliveryStatus = this?.deliveryStatus.orEmpty(),
    )
}