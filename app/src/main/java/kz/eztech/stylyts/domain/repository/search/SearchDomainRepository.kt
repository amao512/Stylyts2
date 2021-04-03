package kz.eztech.stylyts.domain.repository.search

import io.reactivex.Single
import kz.eztech.stylyts.domain.models.UserModel
import kz.eztech.stylyts.domain.models.ResultsModel

/**
 * Created by Ruslan Erdenoff on 22.02.2021.
 */
interface SearchDomainRepository {

    fun getUserByUsername(
        token: String,
        username: String
    ): Single<ResultsModel<UserModel>>
}