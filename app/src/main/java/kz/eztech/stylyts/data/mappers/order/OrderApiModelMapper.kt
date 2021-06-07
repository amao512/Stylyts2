package kz.eztech.stylyts.data.mappers.order

import kz.eztech.stylyts.data.api.models.order.OrderApiModel
import kz.eztech.stylyts.data.mappers.clothes.ClothesApiModelMapper
import kz.eztech.stylyts.data.mappers.user.UserShortApiModelMapper
import kz.eztech.stylyts.domain.models.order.OrderModel
import kz.eztech.stylyts.presentation.utils.EMPTY_STRING
import javax.inject.Inject

class OrderApiModelMapper @Inject constructor(
    private val userShortApiModelMapper: UserShortApiModelMapper,
    private val invoiceApiModelMapper: InvoiceApiModelMapper,
    private val deliveryApiModelMapper: DeliveryApiModelMapper,
    private val customerApiModelMapper: CustomerApiModelMapper,
    private val clothesApiModelMapper: ClothesApiModelMapper
) {

    fun map(data: List<OrderApiModel>?): List<OrderModel> {
        data ?: return emptyList()

        return data.map {
            OrderModel(
                id = it.id ?: 0,
                invoice = invoiceApiModelMapper.map(data = it.invoice),
                price = it.price ?: 0,
                seller = userShortApiModelMapper.map(data = it.seller),
                client = userShortApiModelMapper.map(data = it.client),
                itemObjects = clothesApiModelMapper.map(data = it.itemObjects),
                delivery = deliveryApiModelMapper.map(data = it.delivery),
                customer = customerApiModelMapper.map(data = it.customer),
                status = it.status ?: EMPTY_STRING,
                itemTitles = it.itemTitles ?: emptyList(),
                createdAt = it.createdAt ?: EMPTY_STRING,
                modifiedAt = it.modifiedAt ?: EMPTY_STRING
            )
        }
    }

    fun map(data: OrderApiModel?): OrderModel {
        return OrderModel(
            id = data?.id ?: 0,
            invoice = invoiceApiModelMapper.map(data = data?.invoice),
            price = data?.price ?: 0,
            seller = userShortApiModelMapper.map(data = data?.seller),
            client = userShortApiModelMapper.map(data = data?.client),
            itemObjects = clothesApiModelMapper.map(data = data?.itemObjects),
            delivery = deliveryApiModelMapper.map(data = data?.delivery),
            customer = customerApiModelMapper.map(data = data?.customer),
            status = data?.status ?: EMPTY_STRING,
            itemTitles = data?.itemTitles ?: emptyList(),
            createdAt = data?.createdAt ?: EMPTY_STRING,
            modifiedAt = data?.modifiedAt ?: EMPTY_STRING
        )
    }
}