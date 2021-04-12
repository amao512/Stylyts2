package kz.eztech.stylyts.domain.repository.search

import io.reactivex.Single
import kz.eztech.stylyts.domain.models.ResultsModel
import kz.eztech.stylyts.domain.models.user.UserModel

/**
 * Created by Ruslan Erdenoff on 22.02.2021.
 */
interface SearchDomainRepository {

    fun getUserByUsername(
        token: String,
        username: String
    ): Single<ResultsModel<UserModel>>
}