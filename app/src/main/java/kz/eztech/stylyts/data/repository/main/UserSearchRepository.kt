package kz.eztech.stylyts.data.repository.main

import io.reactivex.Single
import kz.eztech.stylyts.data.api.API
import kz.eztech.stylyts.data.exception.NetworkException
import kz.eztech.stylyts.domain.models.UserSearchModel
import kz.eztech.stylyts.domain.repository.main.UserSearchDomainRepository
import javax.inject.Inject

/**
 * Created by Ruslan Erdenoff on 22.02.2021.
 */
class UserSearchRepository @Inject constructor(
    private var api: API
) : UserSearchDomainRepository {

    override fun getUserByUsername(token: String, username: String): Single<List<UserSearchModel>> {
        return api.searchUserByUsername(token, username).map {
            when (it.isSuccessful) {
                true -> it.body()
                else -> throw NetworkException(it)
            }
        }
    }
}