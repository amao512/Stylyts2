package kz.eztech.stylyts.global.domain.models.clothes

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ClothesBrandModel(
    val id: Int,
    val title: String,
    val website: String,
    val logo: String,
    val createdAt: String,
    val modifiedAt: String
) : Parcelable