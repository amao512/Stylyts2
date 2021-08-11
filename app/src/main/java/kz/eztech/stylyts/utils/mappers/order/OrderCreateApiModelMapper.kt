package kz.eztech.stylyts.utils.mappers.order

import kz.eztech.stylyts.data.api.models.order.OrderCreateApiModel
import kz.eztech.stylyts.data.api.models.order.ResponseOrderCreateApiModel
import kz.eztech.stylyts.domain.models.order.OrderCreateModel
import kz.eztech.stylyts.domain.models.order.ResponseOrderCreateModel
import kz.eztech.stylyts.utils.mappers.map

fun OrderCreateApiModel?.map(): OrderCreateModel {
    return OrderCreateModel(
        id = this?.id ?: 0,
        invoice = this?.invoice.map(),
        paymentType = this?.paymentType.orEmpty(),
        customer = this?.customer.map(),
        itemsMetaData = this?.itemsMetaData.map()
    )
}

fun ResponseOrderCreateApiModel?.map(): ResponseOrderCreateModel {
    return ResponseOrderCreateModel(
        id = this?.id ?: 0,
        invoice = this?.invoice.map(),
        itemObjects = this?.itemObjects ?: emptyList()
    )
}