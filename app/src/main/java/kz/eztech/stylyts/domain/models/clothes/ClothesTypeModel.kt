package kz.eztech.stylyts.domain.models.clothes

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ClothesTypeModel(
    val id: Int,
    val title: String,
    val menCoverPhoto: String,
    val womenCoverPhoto: String
) : Parcelable