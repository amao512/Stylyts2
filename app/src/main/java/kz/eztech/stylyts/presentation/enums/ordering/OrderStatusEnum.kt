package kz.eztech.stylyts.presentation.enums.ordering

enum class OrderStatusEnum(val status: String) {
    ACTIVE(status = "ACTIVE"),
    COMPLETED(status = "COMPLETED"),
    RETURNED(status = "RETURNED"),
    CANCELLED(status = "CANCELLED")
}