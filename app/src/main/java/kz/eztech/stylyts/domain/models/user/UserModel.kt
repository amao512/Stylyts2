package kz.eztech.stylyts.domain.models.user

/**
 * Created by Asylzhan Seytbek on 11.04.2021.
 */
data class UserModel(
    val id: Int,
    val email: String,
    val username: String,
    val avatar: String,
    val firstName: String,
    val lastName: String,
    val isBrand: Boolean,
    val dateOfBirth: String,
    val gender: String,
    val webSite: String,
    val instagram: String,
)