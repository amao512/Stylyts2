package kz.eztech.stylyts.ordering.presentation.order_constructor.enums

enum class DeliveryStatusEnum(val status: String) {
    NEW(status = "NEW"),
    IN_PROGRESS(status = "IN-PROGRESS"),
    DELIVERED(status = "DELIVERED")
}