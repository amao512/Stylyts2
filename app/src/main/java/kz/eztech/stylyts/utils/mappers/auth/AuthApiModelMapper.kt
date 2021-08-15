package kz.eztech.stylyts.utils.mappers.auth

import kz.eztech.stylyts.auth.data.models.AuthApiModel
import kz.eztech.stylyts.utils.mappers.user.map
import kz.eztech.stylyts.auth.domain.models.AuthModel

fun AuthApiModel?.map(): AuthModel {
    return AuthModel(
        token = this?.token.map(),
        user = this?.user.map()
    )
}