package kz.eztech.stylyts.domain.repository.main

import io.reactivex.Single
import kz.eztech.stylyts.domain.models.ProfileModel

/**
 * Created by Ruslan Erdenoff on 22.02.2021.
 */
interface UserSearchDomainRepository {

    fun getUserByUsername(
        token: String,
        username: String
    ): Single<List<ProfileModel>>
}