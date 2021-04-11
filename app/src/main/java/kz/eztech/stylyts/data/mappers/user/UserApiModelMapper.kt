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
}