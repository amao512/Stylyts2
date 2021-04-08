package kz.eztech.stylyts.domain.models.clothes

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ClothesTypeModel(
    @SerializedName("id")
    @Expose
    val id: Int?,
    @SerializedName("title")
    @Expose
    val title: String?,
    @SerializedName("men_cover_photo")
    @Expose
    val menCoverPhoto: String?,
    @SerializedName("women_cover_photo")
    @Expose
    val womenCoverPhoto: String?
) : Parcelable