package kz.eztech.stylyts.data.repository.main

import io.reactivex.Single
import kz.eztech.stylyts.data.api.ProfileApi
import kz.eztech.stylyts.data.exception.NetworkException
import kz.eztech.stylyts.domain.models.ProfileModel
import kz.eztech.stylyts.domain.repository.main.UserSearchDomainRepository
import javax.inject.Inject

/**
 * Created by Ruslan Erdenoff on 22.02.2021.
 */
class UserSearchRepository @Inject constructor(
    private var api: ProfileApi
) : UserSearchDomainRepository {

    override fun getUserByUsername(
        token: String,
        username: String
    ): Single<List<ProfileModel>> {
        return api.searchUserByUsername(token, username).map {
            when (it.isSuccessful) {
                true -> it.body()
                else -> throw NetworkException(it)
            }
        }
    }
}