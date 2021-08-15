package kz.eztech.stylyts.global.domain.repositories

import io.reactivex.Single
import kz.eztech.stylyts.global.domain.models.common.ErrorModel
import kz.eztech.stylyts.auth.domain.models.TokenModel

interface MainDomainRepository {

    fun verifyToken(token: String): Single<ErrorModel>

    fun refreshToken(refresh: String): Single<TokenModel>
}