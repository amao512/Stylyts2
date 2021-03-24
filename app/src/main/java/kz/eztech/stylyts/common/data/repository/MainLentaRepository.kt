package kz.eztech.stylyts.common.data.repository

import io.reactivex.Single
import kz.eztech.stylyts.common.data.api.API
import kz.eztech.stylyts.common.data.exception.NetworkException
import kz.eztech.stylyts.common.domain.models.MainLentaModel
import kz.eztech.stylyts.common.domain.repository.MainLentaDomainRepository
import javax.inject.Inject

/**
 * Created by Ruslan Erdenoff on 14.01.2021.
 */
class MainLentaRepository @Inject constructor(
    private var api: API
) : MainLentaDomainRepository {

    override fun getCollections(token: String, queries: Map<String, Any>?): Single<MainLentaModel> {
        queries?.let {
            return api.getCollections(token, queries).map {
                when (it.isSuccessful) {
                    true -> it.body()
                    false -> throw NetworkException(it)
                }
            }
        } ?: run {
            return api.getCollections(token, HashMap()).map {
                when (it.isSuccessful) {
                    true -> it.body()
                    false -> throw NetworkException(it)
                }
            }
        }
    }
}