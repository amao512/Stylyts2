package kz.eztech.stylyts.data.repository

import io.reactivex.Single
import kz.eztech.stylyts.data.api.network.OutfitsApi
import kz.eztech.stylyts.data.exception.NetworkException
import kz.eztech.stylyts.data.mappers.ResultsApiModelMapper
import kz.eztech.stylyts.data.mappers.outfits.OutfitApiModelMapper
import kz.eztech.stylyts.domain.models.ResultsModel
import kz.eztech.stylyts.domain.models.outfits.OutfitModel
import kz.eztech.stylyts.domain.repository.OutfitsDomainRepository
import okhttp3.MultipartBody
import javax.inject.Inject

class OutfitsRepository @Inject constructor(
    private val api: OutfitsApi,
    private val resultsApiModelMapper: ResultsApiModelMapper,
    private val outfitApiModelMapper: OutfitApiModelMapper
) : OutfitsDomainRepository {

    override fun saveOutfit(
        token: String,
        data: ArrayList<MultipartBody.Part>
    ): Single<OutfitModel> {
        return api.saveOutfit(token, data).map {
            when (it.isSuccessful) {
                true -> outfitApiModelMapper.map(data = it.body())
                false -> throw NetworkException(it)
            }
        }
    }

    override fun getOutfits(
        token: String,
        booleanQueryMap: Map<String, Boolean>,
        stringQueryMap: Map<String, String>
    ): Single<ResultsModel<OutfitModel>> {
        return api.getOutfits(
            token = token,
            booleanQueryMap = booleanQueryMap,
            stringQueryMap = stringQueryMap
        ).map {
            when (it.isSuccessful) {
                true -> resultsApiModelMapper.map(data = it.body())
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

    override fun deleteOutfit(
        token: String,
        outfitId: String
    ): Single<Any> {
        return api.deleteOutfit(token, outfitId).map {
            when (it.isSuccessful) {
                true -> it.body()
                false -> throw NetworkException(it)
            }
        }
    }

    override fun updateOutfit(
        token: String,
        outfitId: String,
        data: ArrayList<MultipartBody.Part>
    ): Single<OutfitModel> {
        return api.updateOutfit(
            token = token,
            outfitId = outfitId,
            files = data
        ).map {
            when (it.isSuccessful) {
                true -> outfitApiModelMapper.map(data = it.body())
                false -> throw NetworkException(it)
            }
        }
    }
}