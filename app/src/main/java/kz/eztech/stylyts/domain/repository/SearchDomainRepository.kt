package kz.eztech.stylyts.domain.repository

import io.reactivex.Single
import kz.eztech.stylyts.domain.models.common.ResultsModel
import kz.eztech.stylyts.domain.models.clothes.ClothesModel
import kz.eztech.stylyts.domain.models.user.UserModel

/**
 * Created by Ruslan Erdenoff on 22.02.2021.
 */
interface SearchDomainRepository {

    fun searchProfileByUsername(
        token: String,
        username: String,
        map: Map<String, String>
    ): Single<ResultsModel<UserModel>>

    fun searchClothesByTitle(
        token: String,
        title: String,
        map: Map<String, String>
    ): Single<ResultsModel<ClothesModel>>
}