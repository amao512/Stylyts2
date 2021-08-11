package kz.eztech.stylyts.data.repository

import io.reactivex.Single
import kz.eztech.stylyts.data.api.network.SearchAPI
import kz.eztech.stylyts.data.exception.NetworkException
import kz.eztech.stylyts.utils.mappers.map
import kz.eztech.stylyts.domain.models.clothes.ClothesModel
import kz.eztech.stylyts.domain.models.common.ResultsModel
import kz.eztech.stylyts.domain.models.user.UserModel
import kz.eztech.stylyts.domain.repository.SearchDomainRepository
import javax.inject.Inject

/**
 * Created by Ruslan Erdenoff on 22.02.2021.
 */
class SearchRepository @Inject constructor(
    private val api: SearchAPI
) : SearchDomainRepository {

    override fun searchProfileByUsername(
        username: String,
        map: Map<String, String>
    ): Single<ResultsModel<UserModel>> {
        return api.searchUserByUsername(
            username = username,
            queryMap = map
        ).map {
            when (it.isSuccessful) {
                true -> it.body().map()
                else -> throw NetworkException(it)
            }
        }
    }

    override fun searchClothesByTitle(
        title: String,
        map: Map<String, String>
    ): Single<ResultsModel<ClothesModel>> {
        return api.searchClothesByTitle(
            title = title,
            queryMap = map
        ).map {
            when (it.isSuccessful) {
                true -> it.body().map()
                else -> throw NetworkException(it)
            }
        }
    }
}