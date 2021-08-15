package kz.eztech.stylyts.global.domain.repositories

import io.reactivex.Single
import kz.eztech.stylyts.global.domain.models.common.ResultsModel
import kz.eztech.stylyts.global.domain.models.clothes.ClothesModel
import kz.eztech.stylyts.global.domain.models.user.UserModel

/**
 * Created by Ruslan Erdenoff on 22.02.2021.
 */
interface SearchDomainRepository {

    fun searchProfileByUsername(
        username: String,
        map: Map<String, String>
    ): Single<ResultsModel<UserModel>>

    fun searchClothesByTitle(
        title: String,
        map: Map<String, String>
    ): Single<ResultsModel<ClothesModel>>
}