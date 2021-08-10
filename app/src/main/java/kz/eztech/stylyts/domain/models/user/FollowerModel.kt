package kz.eztech.stylyts.domain.models.user

import java.util.*

data class FollowerModel(
    val id: Int,
    val username: String,
    val firstName: String,
    val lastName: String,
    val avatar: String,
    var isAlreadyFollow: Boolean
) {
    val displayFullName
        get() = if (lastName.isNotEmpty()) {
            "$firstName $lastName"
        } else {
            firstName
        }

    val displayShortName
        get() = "${firstName.toUpperCase(Locale.getDefault())[0]}${lastName.toUpperCase(Locale.getDefault())[0]}"
}