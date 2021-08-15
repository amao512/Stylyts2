package kz.eztech.stylyts.ordering.presentation.order_constructor.enums

enum class OrderStatusEnum(val status: String) {
    ACTIVE(status = "ACTIVE"),
    COMPLETED(status = "COMPLETED"),
    RETURNED(status = "RETURNED"),
    CANCELLED(status = "CANCELLED")
}