package kz.eztech.stylyts.presentation.utils.mappers.order

import kz.eztech.stylyts.data.api.models.order.OrderCreateApiModel
import kz.eztech.stylyts.domain.models.order.OrderCreateModel
import kz.eztech.stylyts.presentation.utils.mappers.map

fun OrderCreateApiModel?.map(): OrderCreateModel {
    return OrderCreateModel(
        id = this?.id ?: 0,
        invoice = this?.invoice.map(),
        paymentType = this?.paymentType.orEmpty(),
        customer = this?.customer.map(),
        itemsMetaData = this?.itemsMetaData.map()
    )
}