package kz.eztech.stylyts.data.repository

import io.reactivex.Single
import kz.eztech.stylyts.data.api.network.MainApi
import kz.eztech.stylyts.data.exception.NetworkException
import kz.eztech.stylyts.presentation.utils.extensions.mappers.auth.map
import kz.eztech.stylyts.domain.models.auth.TokenModel
import kz.eztech.stylyts.domain.models.common.ErrorModel
import kz.eztech.stylyts.domain.repository.MainDomainRepository
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