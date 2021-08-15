package kz.eztech.stylyts.utils.mappers.order

import kz.eztech.stylyts.ordering.data.models.order.OrderItemApiModel
import kz.eztech.stylyts.ordering.domain.models.order.OrderItemModel

fun List<OrderItemApiModel>?.map(): List<OrderItemModel> {
    this ?: return emptyList()

    return this.map {
        OrderItemModel(
            count = it.count ?: 0,
            clothes = it.clothes ?: 0,
            size = it.size.orEmpty(),
            referralUser = it.referralUser ?: 0
        )
    }
}

fun OrderItemApiModel?.map(): OrderItemModel {
    return OrderItemModel(
        count = this?.count ?: 0,
        clothes = this?.clothes ?: 0,
        size = this?.size.orEmpty(),
        referralUser = this?.referralUser ?: 0
    )
}