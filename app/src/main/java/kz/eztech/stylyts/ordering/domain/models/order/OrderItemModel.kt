package kz.eztech.stylyts.ordering.domain.models.order

data class OrderItemModel(
    val count: Int,
    val size: String,
    val clothes: Int,
    val referralUser: Int
)