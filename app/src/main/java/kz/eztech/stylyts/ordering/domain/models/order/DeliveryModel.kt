package kz.eztech.stylyts.ordering.domain.models.order

data class DeliveryModel(
    val city: String,
    val street: String,
    val house: String,
    val apartment: String,
    val deliveryType: String,
    val deliveryStatus: String
)