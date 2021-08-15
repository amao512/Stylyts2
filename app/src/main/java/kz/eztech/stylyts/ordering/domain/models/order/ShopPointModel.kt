package kz.eztech.stylyts.ordering.domain.models.order

import kz.eztech.stylyts.ordering.data.models.order.DeliveryCreateApiModel
import java.util.*

data class ShopPointModel(
    val id: Int,
    val firstName: String,
    val lastName: String,
    val title: String,
    val avatar: String,
    var selectedAddress: DeliveryCreateApiModel? = null,
    var isSelected: Boolean = false
) {
    val displayFullName
        get() = if (lastName.isNotEmpty()) {
            "$firstName $lastName"
        } else {
            firstName
        }

    val displayShortName
        get() = "${firstName.toUpperCase(Locale.getDefault())[0]}${lastName.toUpperCase(Locale.getDefault())[0]}"
}