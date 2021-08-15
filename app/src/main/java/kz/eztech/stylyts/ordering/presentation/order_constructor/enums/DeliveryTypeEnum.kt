package kz.eztech.stylyts.ordering.presentation.order_constructor.enums

enum class DeliveryTypeEnum(val type: String) {
    COURIER(type = "courier"),
    SELF_PICKUP(type = "self-pickup"),
    POST(type = "post")
}