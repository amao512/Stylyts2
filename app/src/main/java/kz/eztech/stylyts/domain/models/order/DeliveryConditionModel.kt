package kz.eztech.stylyts.domain.models.order

data class DeliveryConditionModel(
    val id: Int,
    val title: String,
    val fittingTime: String,
    val deliveryDate: String
)