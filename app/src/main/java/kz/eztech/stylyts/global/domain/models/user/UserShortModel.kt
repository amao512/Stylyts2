package kz.eztech.stylyts.global.domain.models.user

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class UserShortModel(
    val id: Int,
    val username: String,
    val firstName: String,
    val lastName: String,
    val avatar: String,
    val isAlreadyFollow: Boolean,
    val isBrand: Boolean
): Parcelable {
    val displayFullName
        get() = if (lastName.isNotEmpty()) {
            "$firstName $lastName"
        } else {
            firstName
        }

    val displayShortName
        get() = "${firstName.toUpperCase(Locale.getDefault())[0]}${lastName.toUpperCase(Locale.getDefault())[0]}"
}