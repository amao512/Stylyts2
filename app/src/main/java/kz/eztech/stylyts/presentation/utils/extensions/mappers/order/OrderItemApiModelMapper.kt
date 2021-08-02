package kz.eztech.stylyts.presentation.utils.extensions.mappers.order

import kz.eztech.stylyts.data.api.models.order.OrderItemApiModel
import kz.eztech.stylyts.domain.models.order.OrderItemModel
import kz.eztech.stylyts.presentation.utils.EMPTY_STRING

fun List<OrderItemApiModel>?.map(): List<OrderItemModel> {
    this ?: return emptyList()

    return this.map {
        OrderItemModel(
            count = it.count ?: 0,
            clothes = it.clothes ?: 0,
            size = it.size ?: EMPTY_STRING,
            referralUser = it.referralUser ?: 0
        )
    }
}

fun OrderItemApiModel?.map(): OrderItemModel {
    return OrderItemModel(
        count = this?.count ?: 0,
        clothes = this?.clothes ?: 0,
        size = this?.size ?: EMPTY_STRING,
        referralUser = this?.referralUser ?: 0
    )
}