package kz.eztech.stylyts.utils.mappers.user

import kz.eztech.stylyts.global.data.models.user.UserShortApiModel
import kz.eztech.stylyts.global.domain.models.user.UserShortModel
import kz.eztech.stylyts.utils.extensions.orFalse

/**
 * Created by Asylzhan Seytbek on 11.04.2021.
 */
fun UserShortApiModel?.map(): UserShortModel {
    return UserShortModel(
        id = this?.id ?: 0,
        username = this?.username.orEmpty(),
        firstName = this?.firstName.orEmpty(),
        lastName = this?.lastName.orEmpty(),
        avatar = this?.avatar.orEmpty(),
        isAlreadyFollow = this?.isAlreadyFollow.orFalse(),
        isBrand = this?.isBrand.orFalse()
    )
}