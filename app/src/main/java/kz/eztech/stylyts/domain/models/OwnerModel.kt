package kz.eztech.stylyts.domain.models

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class OwnerModel(
    @SerializedName("id")
    @Expose
    val id: Int?,
    @SerializedName("username")
    @Expose
    val username: String?
): Parcelable