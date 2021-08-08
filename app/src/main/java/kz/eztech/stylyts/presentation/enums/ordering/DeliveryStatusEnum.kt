package kz.eztech.stylyts.presentation.enums.ordering

enum class DeliveryStatusEnum(val status: String) {
    NEW(status = "NEW"),
    IN_PROGRESS(status = "IN-PROGRESS"),
    DELIVERED(status = "DELIVERED")
}