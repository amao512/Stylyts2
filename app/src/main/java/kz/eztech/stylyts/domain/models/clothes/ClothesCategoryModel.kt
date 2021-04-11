package kz.eztech.stylyts.domain.models.clothes

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.IgnoredOnParcel
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ClothesCategoryModel(
    @SerializedName("id")
    @Expose
    val id: Int?,
    @SerializedName("clothes_type")
    @Expose
    val clothesType: ClothesTypeModel?,
    @SerializedName("title")
    @Expose
    val title: String?,
    @SerializedName("body_part")
    @Expose
    val bodyPart: Int?
): Parcelable {
    @IgnoredOnParcel
    var isChecked: Boolean = false
}