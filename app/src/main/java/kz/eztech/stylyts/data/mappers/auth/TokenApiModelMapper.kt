package kz.eztech.stylyts.data.mappers.auth

import kz.eztech.stylyts.data.api.models.auth.TokenApiModel
import kz.eztech.stylyts.domain.models.auth.TokenModel
import kz.eztech.stylyts.presentation.utils.EMPTY_STRING
import javax.inject.Inject

class TokenApiModelMapper @Inject constructor() {

    fun map(data: TokenApiModel?): TokenModel {
        return TokenModel(
            access = data?.access ?: EMPTY_STRING,
            refresh = data?.refresh ?: EMPTY_STRING
        )
    }
}