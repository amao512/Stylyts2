package kz.eztech.stylyts.domain.models.clothes

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ClothesImage(
    @SerializedName("id")
    @Expose
    val id: Int?,
    @SerializedName("image")
    @Expose
    val image: String?,
    @SerializedName("is_cover_photo")
    @Expose
    val isCoverPhoto: Boolean,
    @SerializedName("is_constructor_photo")
    @Expose
    val isConstructorPhoto: Boolean,
    @SerializedName("clothes")
    @Expose
    val clothes: Int?
) : Parcelable