package kz.eztech.stylyts.ordering.presentation.order_constructor.enums

enum class PaymentStatusEnum(val status: String) {
    NEW(status = "NEW"),
    PENDING(status = "PENDING"),
    PAID(status = "PAID"),
    REFUND(status = "REFUND")
}