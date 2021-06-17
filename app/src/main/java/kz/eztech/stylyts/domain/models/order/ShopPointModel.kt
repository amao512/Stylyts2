package kz.eztech.stylyts.domain.models.order

import kz.eztech.stylyts.data.api.models.order.DeliveryCreateApiModel

data class ShopPointModel(
    val id: Int,
    val firstName: String,
    val lastName: String,
    val title: String,
    val avatar: String,
    var selectedAddress: DeliveryCreateApiModel? = null,
    var isSelected: Boolean = false
)