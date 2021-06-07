package kz.eztech.stylyts.domain.models.order

data class OrderCreateModel(
    val id: Int,
    val invoice: InvoiceModel,
    var itemObjects: List<Int>,
    var paymentType: String,
    val customer: CustomerModel
)