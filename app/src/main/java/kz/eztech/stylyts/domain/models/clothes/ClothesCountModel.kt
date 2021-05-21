package kz.eztech.stylyts.domain.models.clothes

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ClothesCountModel(
    val clothesId: Int,
    val count: Int
) : Parcelable