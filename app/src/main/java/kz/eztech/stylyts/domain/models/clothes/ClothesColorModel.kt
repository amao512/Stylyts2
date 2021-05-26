package kz.eztech.stylyts.domain.models.clothes

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ClothesColorModel(
    val id: Int,
    val title: String,
    val color: String
) : Parcelable