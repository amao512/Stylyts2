package kz.eztech.stylyts.domain.models.outfits

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ClothesLocationModel(
    var clothesId: Int,
    var pointX: Double,
    var pointY: Double,
    var width: Double,
    var height: Double,
    var degree: Double,
) : Parcelable