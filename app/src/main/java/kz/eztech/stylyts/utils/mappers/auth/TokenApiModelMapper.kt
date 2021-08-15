package kz.eztech.stylyts.utils.mappers.auth

import kz.eztech.stylyts.auth.data.models.TokenApiModel
import kz.eztech.stylyts.auth.domain.models.TokenModel

fun TokenApiModel?.map(): TokenModel {
    return TokenModel(
        access = this?.access.orEmpty(),
        refresh = this?.refresh.orEmpty()
    )
}