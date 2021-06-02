package kz.eztech.stylyts.presentation.enums.ordering

enum class DeliveryTypeEnum(val type: String) {
    COURIER(type = "courier"),
    SELF_PICKUP(type = "self-pickup"),
    POST(type = "post")
}