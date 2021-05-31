package kz.eztech.stylyts.data.api.models.order

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CustomerApiModel(
    @SerializedName("first_name")
    @Expose
    val firstName: String?,
    @SerializedName("last_name")
    @Expose
    val lastName: String?,
    @SerializedName("phone_number")
    @Expose
    val phoneNumber: String?,
    @SerializedName("email")
    @Expose
    val email: String?
) : Parcelable