package kz.eztech.stylyts.data.repository.outfits

import io.reactivex.Single
import kz.eztech.stylyts.data.api.network.OutfitsApi
import kz.eztech.stylyts.data.exception.NetworkException
import kz.eztech.stylyts.data.mappers.ResultsApiModelMapper
import kz.eztech.stylyts.domain.models.ResultsModel
import kz.eztech.stylyts.domain.models.outfits.OutfitModel
import kz.eztech.stylyts.domain.repository.outfits.OutfitsDomainRepository
import javax.inject.Inject

class OutfitsRepository @Inject constructor(
    private val api: OutfitsApi,
    private val resultsApiModelMapper: ResultsApiModelMapper
) : OutfitsDomainRepository {

    override fun getOutfits(token: String): Single<ResultsModel<OutfitModel>> {
        return api.getOutfits(token).map {
            when (it.isSuccessful) {
                true -> resultsApiModelMapper.mapOutfitResults(data = it.body())
                false -> throw NetworkException(it)
            }
        }
    }
}