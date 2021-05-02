package kz.eztech.stylyts.data.mappers.user

import kz.eztech.stylyts.data.api.models.user.UserApiModel
import kz.eztech.stylyts.domain.models.user.UserModel
import kz.eztech.stylyts.presentation.enums.GenderEnum
import kz.eztech.stylyts.presentation.utils.EMPTY_STRING
import javax.inject.Inject

/**
 * Created by Asylzhan Seytbek on 11.04.2021.
 */
class UserApiModelMapper @Inject constructor() {

    fun map(data: UserApiModel?): UserModel {
        return UserModel(
            id = data?.id ?: 0,
            email = data?.email ?: EMPTY_STRING,
            username = data?.username ?: EMPTY_STRING,
            avatar = data?.avatar ?: EMPTY_STRING,
            firstName = data?.firstName ?: EMPTY_STRING,
            lastName = data?.lastName ?: EMPTY_STRING,
            isBrand = data?.isBrand ?: false,
            dateOfBirth = data?.dateOfBirth ?: EMPTY_STRING,
            gender = setGender(gender = data?.gender),
            webSite = data?.webSite ?: EMPTY_STRING,
            instagram = data?.instagram ?: EMPTY_STRING,
            followersCount = data?.followersCount ?: 0,
            followingsCount = data?.followingsCount ?: 0,
            outfitsCount = data?.outfitsCount ?: 0
        )
    }

    fun map(data: List<UserApiModel>?): List<UserModel> {
        data ?: return emptyList()

        return data.map {
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
}