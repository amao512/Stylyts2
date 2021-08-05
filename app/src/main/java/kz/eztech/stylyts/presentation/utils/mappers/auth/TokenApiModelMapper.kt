package kz.eztech.stylyts.presentation.utils.mappers.auth

import kz.eztech.stylyts.data.api.models.auth.TokenApiModel
import kz.eztech.stylyts.domain.models.auth.TokenModel

fun TokenApiModel?.map(): TokenModel {
    return TokenModel(
        access = this?.access.orEmpty(),
        refresh = this?.refresh.orEmpty()
    )
}