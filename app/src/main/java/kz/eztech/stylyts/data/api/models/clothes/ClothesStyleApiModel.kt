package kz.eztech.stylyts.data.api.models.clothes

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ClothesStyleApiModel(
    @SerializedName("id")
    @Expose
    val id: Int?,
    @SerializedName("title")
    @Expose
    val title: String?
): Parcelable