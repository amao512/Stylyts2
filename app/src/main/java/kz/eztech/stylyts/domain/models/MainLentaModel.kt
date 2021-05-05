package kz.eztech.stylyts.domain.models

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

/**
 * Created by Ruslan Erdenoff on 14.01.2021.
 */

@Parcelize
data class ClothesColor(
    @SerializedName("id")
    @Expose
    var id: Int? = null,
    @SerializedName("color")
    @Expose
    var color: String? = null
) : Parcelable