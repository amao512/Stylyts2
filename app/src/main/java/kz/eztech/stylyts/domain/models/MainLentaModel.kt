package kz.eztech.stylyts.domain.models

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import kz.eztech.stylyts.domain.models.shop.ClothesTypes

/**
 * Created by Ruslan Erdenoff on 14.01.2021.
 */

@Parcelize
data class ClothesSize(
    @SerializedName("id")
    @Expose
    var id: Int? = null,
    @SerializedName("size")
    @Expose
    var size: String? = null
) : Parcelable

@Parcelize
data class ClothesColor(
    @SerializedName("id")
    @Expose
    var id: Int? = null,
    @SerializedName("color")
    @Expose
    var color: String? = null
) : Parcelable