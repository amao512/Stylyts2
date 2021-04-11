package kz.eztech.stylyts.domain.models.clothes

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.IgnoredOnParcel
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ClothesBrandModel(
    @SerializedName("id")
    @Expose
    val id: Int?,
    @SerializedName("title")
    @Expose
    val title: String?,
    @SerializedName("website")
    @Expose
    val website: String?,
    @SerializedName("logo")
    @Expose
    val logo: String?,
    @SerializedName("created_at")
    @Expose
    val createdAt: String?,
    @SerializedName("modified_at")
    @Expose
    val modifiedAt: String?
) : Parcelable {
    @IgnoredOnParcel
    var isChecked: Boolean = false
}