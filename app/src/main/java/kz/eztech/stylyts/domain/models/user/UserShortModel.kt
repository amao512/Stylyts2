package kz.eztech.stylyts.domain.models.user

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UserShortModel(
    val id: Int,
    val username: String,
    val firstName: String,
    val lastName: String,
    val avatar: String,
    val isAlreadyFollow: Boolean,
    val isBrand: Boolean
): Parcelable