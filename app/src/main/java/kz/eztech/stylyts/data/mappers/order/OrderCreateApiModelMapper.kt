package kz.eztech.stylyts.data.mappers.order

import kz.eztech.stylyts.data.api.models.order.OrderCreateApiModel
import kz.eztech.stylyts.domain.models.order.OrderCreateModel
import kz.eztech.stylyts.presentation.utils.EMPTY_STRING
import javax.inject.Inject

class OrderCreateApiModelMapper @Inject constructor(
    private val invoiceApiModelMapper: InvoiceApiModelMapper,
    private val customerApiModelMapper: CustomerApiModelMapper
) {

    fun map(data: OrderCreateApiModel?): OrderCreateModel {
        return OrderCreateModel(
            id = data?.id ?: 0,
            invoice = invoiceApiModelMapper.map(data = data?.invoice),
            itemObjects = data?.itemObjects ?: emptyList(),
            paymentType = data?.paymentType ?: EMPTY_STRING,
            customer = customerApiModelMapper.map(data = data?.customer)
        )
    }
}