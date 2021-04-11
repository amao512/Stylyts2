package kz.eztech.stylyts.domain.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class OwnerModel(
    val id: Int,
    val username: String
): Parcelable