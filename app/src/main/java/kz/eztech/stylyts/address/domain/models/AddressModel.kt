package kz.eztech.stylyts.address.domain.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by Asylzhan Seytbek on 14.03.2021.
 */
data class AddressModel(
    @SerializedName("id")
    @Expose
    val id: String?,
    @SerializedName("country")
    @Expose
    val country: String?,
    @SerializedName("city")
    @Expose
    val city: String?,
    @SerializedName("street")
    @Expose
    val street: String?,
    @SerializedName("apartment")
    @Expose
    val apartment: String?,
    @SerializedName("entrance")
    @Expose
    val entrance: String?,
    @SerializedName("floor")
    @Expose
    val floor: String?,
    @SerializedName("door_phone")
    @Expose
    val doorPhone: String?,
    @SerializedName("postal_code")
    @Expose
    val postalCode: String?,
    @SerializedName("comment")
    @Expose
    val comment: String?
) {
    var isDefaultAddress: Boolean = false
}