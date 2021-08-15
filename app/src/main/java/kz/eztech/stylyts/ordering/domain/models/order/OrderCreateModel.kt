package kz.eztech.stylyts.ordering.domain.models.order

data class OrderCreateModel(
    val id: Int,
    val invoice: InvoiceModel,
    var paymentType: String,
    val customer: CustomerModel,
    val itemsMetaData: List<OrderItemModel>
)

data class ResponseOrderCreateModel(
    val id: Int,
    val invoice: InvoiceModel,
    val itemObjects: List<Int>
)