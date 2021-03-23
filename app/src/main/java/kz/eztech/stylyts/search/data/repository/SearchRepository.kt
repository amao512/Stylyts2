package kz.eztech.stylyts.search.data.repository

import io.reactivex.Single
import kz.eztech.stylyts.data.exception.NetworkException
import kz.eztech.stylyts.domain.models.UserModel
import kz.eztech.stylyts.search.data.SearchAPI
import kz.eztech.stylyts.search.domain.models.SearchModel
import kz.eztech.stylyts.search.domain.repository.SearchDomainRepository
import javax.inject.Inject

/**
 * Created by Ruslan Erdenoff on 22.02.2021.
 */
class SearchRepository @Inject constructor(
    private var api: SearchAPI
) : SearchDomainRepository {

    override fun getUserByUsername(
        token: String,
        username: String
    ): Single<SearchModel<UserModel>> {
        return api.searchUserByUsername(token, username).map {
            when (it.isSuccessful) {
                true -> it.body()
                else -> throw NetworkException(it)
            }
        }
    }
}