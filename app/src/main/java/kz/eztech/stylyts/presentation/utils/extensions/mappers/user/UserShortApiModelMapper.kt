package kz.eztech.stylyts.presentation.utils.extensions.mappers.user

import kz.eztech.stylyts.data.api.models.user.UserShortApiModel
import kz.eztech.stylyts.domain.models.user.UserShortModel
import kz.eztech.stylyts.presentation.utils.EMPTY_STRING

/**
 * Created by Asylzhan Seytbek on 11.04.2021.
 */
fun UserShortApiModel?.map(): UserShortModel {
    return UserShortModel(
        id = this?.id ?: 0,
        username = this?.username ?: EMPTY_STRING,
        firstName = this?.firstName ?: EMPTY_STRING,
        lastName = this?.lastName ?: EMPTY_STRING,
        avatar = this?.avatar ?: EMPTY_STRING,
        isAlreadyFollow = this?.isAlreadyFollow ?: false,
        isBrand = this?.isBrand ?: false
    )
}