package kz.eztech.stylyts.presentation.enums.ordering

enum class PaymentStatusEnum(val status: String) {
    NEW(status = "NEW"),
    PENDING(status = "PENDING"),
    PAID(status = "PAID"),
    REFUND(status = "REFUND")
}