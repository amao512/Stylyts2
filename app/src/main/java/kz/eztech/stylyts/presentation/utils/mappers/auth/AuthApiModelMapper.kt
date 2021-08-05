package kz.eztech.stylyts.presentation.utils.mappers.auth

import kz.eztech.stylyts.data.api.models.auth.AuthApiModel
import kz.eztech.stylyts.presentation.utils.mappers.user.map
import kz.eztech.stylyts.domain.models.auth.AuthModel

fun AuthApiModel?.map(): AuthModel {
    return AuthModel(
        token = this?.token.map(),
        user = this?.user.map()
    )
}