package kz.eztech.stylyts.presentation.utils.extensions.mappers.order

import kz.eztech.stylyts.data.api.models.order.InvoiceApiModel
import kz.eztech.stylyts.domain.models.order.InvoiceModel
import kz.eztech.stylyts.presentation.utils.EMPTY_STRING

fun InvoiceApiModel?.map(): InvoiceModel {
    return InvoiceModel(
        operationId = this?.operationId ?: EMPTY_STRING,
        operationUrl = this?.operationUrl ?: EMPTY_STRING,
        invoiceId = this?.invoiceId ?: EMPTY_STRING,
        paymentStatus = this?.paymentStatus ?: EMPTY_STRING
    )
}