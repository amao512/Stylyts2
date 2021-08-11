package kz.eztech.stylyts.presentation.utils.mappers.order

import kz.eztech.stylyts.data.api.models.order.OrderApiModel
import kz.eztech.stylyts.domain.models.order.OrderModel
import kz.eztech.stylyts.presentation.utils.extensions.getZonedDateTime
import kz.eztech.stylyts.presentation.utils.mappers.clothes.map
import kz.eztech.stylyts.presentation.utils.mappers.user.map

fun List<OrderApiModel>?.map(): List<OrderModel> {
    this ?: return emptyList()

    return this.map {
        OrderModel(
            id = it.id ?: 0,
            invoice = it.invoice.map(),
            price = it.price ?: 0,
            seller = it.seller.map(),
            client = it.client.map(),
            itemObjects = it.itemObjects.map(),
            delivery = it.delivery.map(),
            customer = it.customer.map(),
            status = it.status.orEmpty(),
            itemTitles = it.itemTitles ?: emptyList(),
            createdAt = it.createdAt.orEmpty().getZonedDateTime(),
            modifiedAt = it.modifiedAt.orEmpty().getZonedDateTime(),
            itemsMetaData = it.itemsMetaData.map()
        )
    }
}

fun OrderApiModel?.map(): OrderModel {
    return OrderModel(
        id = this?.id ?: 0,
        invoice = this?.invoice.map(),
        price = this?.price ?: 0,
        seller = this?.seller.map(),
        client = this?.client.map(),
        itemObjects = this?.itemObjects.map(),
        delivery = this?.delivery.map(),
        customer = this?.customer.map(),
        status = this?.status.orEmpty(),
        itemTitles = this?.itemTitles ?: emptyList(),
        createdAt = this?.createdAt.orEmpty().getZonedDateTime(),
        modifiedAt = this?.modifiedAt.orEmpty().getZonedDateTime(),
        itemsMetaData = this?.itemsMetaData.map()
    )
}