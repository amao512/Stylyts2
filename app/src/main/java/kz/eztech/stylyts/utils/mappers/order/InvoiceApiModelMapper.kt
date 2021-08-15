package kz.eztech.stylyts.utils.mappers.order

import kz.eztech.stylyts.ordering.data.models.order.InvoiceApiModel
import kz.eztech.stylyts.ordering.domain.models.order.InvoiceModel

fun InvoiceApiModel?.map(): InvoiceModel {
    return InvoiceModel(
        operationId = this?.operationId.orEmpty(),
        operationUrl = this?.operationUrl.orEmpty(),
        invoiceId = this?.invoiceId.orEmpty(),
        paymentStatus = this?.paymentStatus.orEmpty()
    )
}