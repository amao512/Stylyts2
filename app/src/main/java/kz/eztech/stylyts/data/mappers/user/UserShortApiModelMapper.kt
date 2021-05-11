package kz.eztech.stylyts.data.mappers.user

import kz.eztech.stylyts.data.api.models.user.UserShortApiModel
import kz.eztech.stylyts.domain.models.user.UserShortModel
import kz.eztech.stylyts.presentation.utils.EMPTY_STRING
import javax.inject.Inject

/**
 * Created by Asylzhan Seytbek on 11.04.2021.
 */
class UserShortApiModelMapper @Inject constructor() {

    fun map(data: UserShortApiModel?): UserShortModel {
        return UserShortModel(
            id = data?.id ?: 0,
            username = data?.username ?: EMPTY_STRING,
            firstName = data?.firstName ?: EMPTY_STRING,
            lastName = data?.lastName ?: EMPTY_STRING,
            avatar = data?.avatar ?: EMPTY_STRING,
            isAlreadyFollow = data?.isAlreadyFollow ?: false,
            isBrand = data?.isBrand ?: false
        )
    }
}