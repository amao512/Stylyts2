package kz.eztech.stylyts.domain.repository.search

import io.reactivex.Single
import kz.eztech.stylyts.data.api.models.user.UserApiModel
import kz.eztech.stylyts.data.api.models.ResultsApiModel

/**
 * Created by Ruslan Erdenoff on 22.02.2021.
 */
interface SearchDomainRepository {

    fun getUserByUsername(
        token: String,
        username: String
    ): Single<ResultsApiModel<UserApiModel>>
}