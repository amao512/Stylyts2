package kz.eztech.stylyts.global.data.models.address

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by Asylzhan Seytbek on 14.03.2021.
 */
data class AddressApiModel(
    @SerializedName("id")
    @Expose
    val id: Int?,
    @SerializedName("user")
    @Expose
    val user: String?,
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
    val comment: String?,
    @SerializedName("house")
    @Expose
    val house: String?
)