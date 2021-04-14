package kz.eztech.stylyts.data.repository.outfits

import io.reactivex.Single
import kz.eztech.stylyts.data.api.network.OutfitsApi
import kz.eztech.stylyts.data.exception.NetworkException
import kz.eztech.stylyts.data.mappers.ResultsApiModelMapper
import kz.eztech.stylyts.data.mappers.outfits.OutfitApiModelMapper
import kz.eztech.stylyts.domain.models.ResultsModel
import kz.eztech.stylyts.domain.models.outfits.OutfitModel
import kz.eztech.stylyts.domain.repository.outfits.OutfitsDomainRepository
import javax.inject.Inject

class OutfitsRepository @Inject constructor(
    private val api: OutfitsApi,
    private val resultsApiModelMapper: ResultsApiModelMapper,
    private val outfitApiModelMapper: OutfitApiModelMapper
) : OutfitsDomainRepository {

    override fun getOutfits(
        token: String,
        booleanQueryMap: Map<String, Boolean>
    ): Single<ResultsModel<OutfitModel>> {
        return api.getOutfits(token, booleanQueryMap).map {
            when (it.isSuccessful) {
                true -> resultsApiModelMapper.mapOutfitResults(data = it.body())
                false -> throw NetworkException(it)
            }
        }
    }

    override fun getOutfitById(
        token: String,
        outfitId: String
    ): Single<OutfitModel> {
        return api.getOutfitById(token, outfitId).map {
            when (it.isSuccessful) {
                true -> outfitApiModelMapper.map(data = it.body())
                false -> throw NetworkException(it)
            }
        }
    }
}