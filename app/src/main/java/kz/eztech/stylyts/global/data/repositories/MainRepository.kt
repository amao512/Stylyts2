package kz.eztech.stylyts.global.data.repositories

import io.reactivex.Single
import kz.eztech.stylyts.global.data.api.MainApi
import kz.eztech.stylyts.global.data.exception.NetworkException
import kz.eztech.stylyts.utils.mappers.auth.map
import kz.eztech.stylyts.auth.domain.models.TokenModel
import kz.eztech.stylyts.global.domain.models.common.ErrorModel
import kz.eztech.stylyts.global.domain.repositories.MainDomainRepository
import javax.inject.Inject

class MainRepository @Inject constructor(
    private val api: MainApi
) : MainDomainRepository {

    override fun verifyToken(token: String): Single<ErrorModel> {
        return api.verifyToken(token).map {
            when (it.isSuccessful) {
                true -> it.body()
                false -> throw NetworkException(it)
            }
        }
    }

    override fun refreshToken(refresh: String): Single<TokenModel> {
        return api.refreshToken(refresh).map {
            when (it.isSuccessful) {
                true -> it.body().map()
                false -> throw NetworkException(it)
            }
        }
    }
}