package kz.eztech.stylyts.domain.models.order

data class OrderItemModel(
    val count: Int,
    val size: String,
    val clothes: Int,
    val referralUser: Int
)