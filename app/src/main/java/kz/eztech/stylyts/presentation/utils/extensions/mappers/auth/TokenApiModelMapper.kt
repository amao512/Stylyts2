package kz.eztech.stylyts.presentation.utils.extensions.mappers.auth

import kz.eztech.stylyts.data.api.models.auth.TokenApiModel
import kz.eztech.stylyts.domain.models.auth.TokenModel
import kz.eztech.stylyts.presentation.utils.EMPTY_STRING

fun TokenApiModel?.map(): TokenModel {
    return TokenModel(
        access = this?.access ?: EMPTY_STRING,
        refresh = this?.refresh ?: EMPTY_STRING
    )
}