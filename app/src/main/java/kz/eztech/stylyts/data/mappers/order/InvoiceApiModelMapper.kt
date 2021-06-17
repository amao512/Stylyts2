package kz.eztech.stylyts.data.mappers.order

import kz.eztech.stylyts.data.api.models.order.InvoiceApiModel
import kz.eztech.stylyts.domain.models.order.InvoiceModel
import kz.eztech.stylyts.presentation.utils.EMPTY_STRING
import javax.inject.Inject

class InvoiceApiModelMapper @Inject constructor() {

    fun map(data: InvoiceApiModel?): InvoiceModel {
        return InvoiceModel(
            operationId = data?.operationId ?: EMPTY_STRING,
            operationUrl = data?.operationUrl ?: EMPTY_STRING,
            invoiceId = data?.invoiceId ?: EMPTY_STRING,
            paymentStatus = data?.paymentStatus ?: EMPTY_STRING
        )
    }
}