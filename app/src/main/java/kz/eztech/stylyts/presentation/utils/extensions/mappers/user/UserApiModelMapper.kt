package kz.eztech.stylyts.presentation.utils.extensions.mappers.user

import kz.eztech.stylyts.data.api.models.user.UserApiModel
import kz.eztech.stylyts.domain.models.user.UserModel
import kz.eztech.stylyts.presentation.enums.GenderEnum
import kz.eztech.stylyts.presentation.utils.EMPTY_STRING

/**
 * Created by Asylzhan Seytbek on 11.04.2021.
 */
fun UserApiModel?.map(): UserModel {
    return UserModel(
        id = this?.id ?: 0,
        email = this?.email ?: EMPTY_STRING,
        username = this?.username ?: EMPTY_STRING,
        avatar = this?.avatar ?: EMPTY_STRING,
        firstName = this?.firstName ?: EMPTY_STRING,
        lastName = this?.lastName ?: EMPTY_STRING,
        isBrand = this?.isBrand ?: false,
        dateOfBirth = this?.dateOfBirth ?: EMPTY_STRING,
        gender = setGender(gender = this?.gender),
        webSite = this?.webSite ?: EMPTY_STRING,
        instagram = this?.instagram ?: EMPTY_STRING,
        followersCount = this?.followersCount ?: 0,
        followingsCount = this?.followingsCount ?: 0,
        outfitsCount = this?.outfitsCount ?: 0
    )
}

fun List<UserApiModel>?.map(): List<UserModel> {
    this ?: return emptyList()

    return this.map {
        UserModel(
            id = it.id ?: 0,
            email = it.email ?: EMPTY_STRING,
            username = it.username ?: EMPTY_STRING,
            avatar = it.avatar ?: EMPTY_STRING,
            firstName = it.firstName ?: EMPTY_STRING,
            lastName = it.lastName ?: EMPTY_STRING,
            isBrand = it.isBrand,
            dateOfBirth = it.dateOfBirth ?: EMPTY_STRING,
            gender = setGender(gender = it.gender),
            webSite = it.webSite ?: EMPTY_STRING,
            instagram = it.instagram ?: EMPTY_STRING,
            followersCount = it.followersCount ?: 0,
            followingsCount = it.followingsCount ?: 0,
            outfitsCount = it.outfitsCount ?: 0
        )
    }
}

private fun setGender(gender: String?): String {
    return when (gender) {
        "male" -> GenderEnum.MALE.gender
        else -> GenderEnum.FEMALE.gender
    }
}