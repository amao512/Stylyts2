package kz.eztech.stylyts.utils.mappers.order

import kz.eztech.stylyts.ordering.data.models.order.DeliveryApiModel
import kz.eztech.stylyts.ordering.domain.models.order.DeliveryModel

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