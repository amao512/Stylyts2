package kz.eztech.stylyts.global.domain.models.clothes

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ClothesStyleModel(
    val id: Int,
    val title: String
): Parcelable