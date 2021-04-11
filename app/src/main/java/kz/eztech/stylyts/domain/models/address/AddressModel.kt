package kz.eztech.stylyts.domain.models.address

/**
 * Created by Asylzhan Seytbek on 11.04.2021.
 */
data class AddressModel(
    val id: Int,
    val user: String,
    val country: String,
    val city: String,
    val street: String,
    val apartment: String,
    val entrance: String,
    val floor: String,
    val doorPhone: String,
    val postalCode: String,
    val comment: String
) {
    var isDefaultAddress: Boolean = false
}