package kz.eztech.stylyts.ordering.domain.models.order

data class InvoiceModel(
    val operationUrl: String,
    val operationId: String,
    val invoiceId: String,
    val paymentStatus: String
)