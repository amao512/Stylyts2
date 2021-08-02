package kz.eztech.stylyts.domain.models.order

import kz.eztech.stylyts.domain.models.clothes.ClothesModel
import kz.eztech.stylyts.domain.models.user.UserShortModel

data class OrderModel(
    val id: Int,
    val invoice: InvoiceModel,
    val price: Int,
    val seller: UserShortModel,
    val client: UserShortModel,
    val itemObjects: List<ClothesModel>,
    val delivery: DeliveryModel,
    val customer: CustomerModel,
    val status: String,
    val itemTitles: List<String>,
    val createdAt: String,
    val modifiedAt: String,
    val itemsMetaData: List<OrderItemModel>,
)