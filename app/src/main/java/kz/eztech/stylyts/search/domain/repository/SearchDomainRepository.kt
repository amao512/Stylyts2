package kz.eztech.stylyts.search.domain.repository

import io.reactivex.Single
import kz.eztech.stylyts.search.domain.models.SearchModel
import kz.eztech.stylyts.common.domain.models.UserModel

/**
 * Created by Ruslan Erdenoff on 22.02.2021.
 */
interface SearchDomainRepository {

    fun getUserByUsername(
        token: String,
        username: String
    ): Single<SearchModel<UserModel>>
}