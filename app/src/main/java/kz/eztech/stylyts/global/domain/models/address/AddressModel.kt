package kz.eztech.stylyts.global.domain.models.address

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Created by Asylzhan Seytbek on 11.04.2021.
 */
@Parcelize
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
    val comment: String,
    val house: String
) : Parcelable {
    var isDefaultAddress: Boolean = false

    val displayAddress
        get() = "$city, $street $house"

    val displayCityAndStreet
        get() = "$city/$street"
}