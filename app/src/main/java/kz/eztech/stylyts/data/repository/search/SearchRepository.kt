package kz.eztech.stylyts.data.repository.search

import io.reactivex.Single
import kz.eztech.stylyts.data.api.network.SearchAPI
import kz.eztech.stylyts.data.exception.NetworkException
import kz.eztech.stylyts.data.mappers.ResultsApiModelMapper
import kz.eztech.stylyts.domain.models.ResultsModel
import kz.eztech.stylyts.domain.models.clothes.ClothesModel
import kz.eztech.stylyts.domain.models.user.UserModel
import kz.eztech.stylyts.domain.repository.search.SearchDomainRepository
import javax.inject.Inject

/**
 * Created by Ruslan Erdenoff on 22.02.2021.
 */
class SearchRepository @Inject constructor(
    private val api: SearchAPI,
    private val resultsApiModelMapper: ResultsApiModelMapper
) : SearchDomainRepository {

    override fun getUserByUsername(
        token: String,
        username: String
    ): Single<ResultsModel<UserModel>> {
        return api.searchUserByUsername(token, username).map {
            when (it.isSuccessful) {
                true -> resultsApiModelMapper.mapUserResults(it.body())
                else -> throw NetworkException(it)
            }
        }
    }

    override fun searchClothesByTitle(
        token: String,
        title: String
    ): Single<ResultsModel<ClothesModel>> {
        return api.searchClothesByTitle(token, title).map {
            when (it.isSuccessful) {
                true -> resultsApiModelMapper.mapClothesResults(it.body())
                else -> throw NetworkException(it)
            }
        }
    }
}