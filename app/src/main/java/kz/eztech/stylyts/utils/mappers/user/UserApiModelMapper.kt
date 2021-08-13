package kz.eztech.stylyts.utils.mappers.user

import kz.eztech.stylyts.data.api.models.user.UserApiModel
import kz.eztech.stylyts.domain.models.user.UserModel
import kz.eztech.stylyts.presentation.enums.GenderEnum
import kz.eztech.stylyts.utils.extensions.orFalse

/**
 * Created by Asylzhan Seytbek on 11.04.2021.
 */
fun UserApiModel?.map(): UserModel {
    return UserModel(
        id = this?.id ?: 0,
        email = this?.email.orEmpty(),
        username = this?.username.orEmpty(),
        avatar = this?.avatar.orEmpty(),
        firstName = this?.firstName.orEmpty(),
        lastName = this?.lastName.orEmpty(),
        isBrand = this?.isBrand.orFalse(),
        dateOfBirth = this?.dateOfBirth.orEmpty(),
        gender = setGender(gender = this?.gender),
        webSite = this?.webSite.orEmpty(),
        instagram = this?.instagram.orEmpty(),
        followersCount = this?.followersCount ?: 0,
        followingsCount = this?.followingsCount ?: 0,
        outfitsCount = this?.outfitsCount ?: 0,
        initial = this?.initials.orEmpty()
    )
}

fun List<UserApiModel>?.map(): List<UserModel> {
    this ?: return emptyList()

    return this.map {
        UserModel(
            id = it.id ?: 0,
            email = it.email.orEmpty(),
            username = it.username.orEmpty(),
            avatar = it.avatar.orEmpty(),
            firstName = it.firstName.orEmpty(),
            lastName = it.lastName.orEmpty(),
            isBrand = it.isBrand,
            dateOfBirth = it.dateOfBirth.orEmpty(),
            gender = setGender(gender = it.gender),
            webSite = it.webSite.orEmpty(),
            instagram = it.instagram.orEmpty(),
            followersCount = it.followersCount ?: 0,
            followingsCount = it.followingsCount ?: 0,
            outfitsCount = it.outfitsCount ?: 0,
            initial = it.initials.orEmpty()
        )
    }
}

private fun setGender(gender: String?): String {
    return when (gender) {
        "male" -> GenderEnum.MALE.gender
        else -> GenderEnum.FEMALE.gender
    }
}