package kz.eztech.stylyts.domain.repository

import io.reactivex.Single
import kz.eztech.stylyts.domain.models.common.ErrorModel
import kz.eztech.stylyts.domain.models.auth.TokenModel

interface MainDomainRepository {

    fun verifyToken(token: String): Single<ErrorModel>

    fun refreshToken(refresh: String): Single<TokenModel>
}