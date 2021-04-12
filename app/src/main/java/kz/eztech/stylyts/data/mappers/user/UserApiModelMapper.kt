package kz.eztech.stylyts.data.mappers.user

import kz.eztech.stylyts.data.api.models.user.UserApiModel
import kz.eztech.stylyts.domain.models.user.UserModel
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
            gender = data?.gender ?: "M",
            webSite = data?.webSite ?: EMPTY_STRING,
            instagram = data?.instagram ?: EMPTY_STRING
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
                gender = it.gender ?: "M",
                webSite = it.webSite ?: EMPTY_STRING,
                instagram = it.instagram ?: EMPTY_STRING
            )
        }
    }
}