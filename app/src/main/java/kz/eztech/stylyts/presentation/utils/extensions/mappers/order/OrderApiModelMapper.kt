package kz.eztech.stylyts.presentation.utils.extensions.mappers.order

import kz.eztech.stylyts.data.api.models.order.OrderApiModel
import kz.eztech.stylyts.presentation.utils.extensions.mappers.clothes.map
import kz.eztech.stylyts.presentation.utils.extensions.mappers.user.map
import kz.eztech.stylyts.domain.models.order.OrderModel
import kz.eztech.stylyts.presentation.utils.EMPTY_STRING

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
            status = it.status ?: EMPTY_STRING,
            itemTitles = it.itemTitles ?: emptyList(),
            createdAt = it.createdAt ?: EMPTY_STRING,
            modifiedAt = it.modifiedAt ?: EMPTY_STRING,
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
        status = this?.status ?: EMPTY_STRING,
        itemTitles = this?.itemTitles ?: emptyList(),
        createdAt = this?.createdAt ?: EMPTY_STRING,
        modifiedAt = this?.modifiedAt ?: EMPTY_STRING,
        itemsMetaData = this?.itemsMetaData.map()
    )
}