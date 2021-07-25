package kz.eztech.stylyts.data.repository

import io.reactivex.Single
import kz.eztech.stylyts.data.api.network.OutfitsApi
import kz.eztech.stylyts.data.exception.NetworkException
import kz.eztech.stylyts.presentation.utils.extensions.mappers.map
import kz.eztech.stylyts.presentation.utils.extensions.mappers.outfits.map
import kz.eztech.stylyts.domain.models.common.ResultsModel
import kz.eztech.stylyts.domain.models.outfits.OutfitCreateModel
import kz.eztech.stylyts.domain.models.outfits.OutfitModel
import kz.eztech.stylyts.domain.repository.OutfitsDomainRepository
import okhttp3.MultipartBody
import javax.inject.Inject

class OutfitsRepository @Inject constructor(
    private val api: OutfitsApi
) : OutfitsDomainRepository {

    override fun saveOutfit(data: ArrayList<MultipartBody.Part>): Single<OutfitCreateModel> {
        return api.saveOutfit(data).map {
            when (it.isSuccessful) {
                true -> it.body().map()
                false -> throw NetworkException(it)
            }
        }
    }

    override fun getOutfits(
        booleanQueryMap: Map<String, Boolean>,
        stringQueryMap: Map<String, String>
    ): Single<ResultsModel<OutfitModel>> {
        return api.getOutfits(
            booleanQueryMap = booleanQueryMap,
            stringQueryMap = stringQueryMap
        ).map {
            when (it.isSuccessful) {
                true -> it.body().map()
                false -> throw NetworkException(it)
            }
        }
    }

    override fun getOutfitById(outfitId: String): Single<OutfitModel> {
        return api.getOutfitById(outfitId).map {
            when (it.isSuccessful) {
                true -> it.body().map()
                false -> throw NetworkException(it)
            }
        }
    }

    override fun deleteOutfit(outfitId: String): Single<Any> {
        return api.deleteOutfit(outfitId).map {
            when (it.isSuccessful) {
                true -> it.body()
                false -> throw NetworkException(it)
            }
        }
    }

    override fun updateOutfit(
        outfitId: String,
        data: ArrayList<MultipartBody.Part>
    ): Single<OutfitCreateModel> {
        return api.updateOutfit(
            outfitId = outfitId,
            files = data
        ).map {
            when (it.isSuccessful) {
                true -> it.body().map()
                false -> throw NetworkException(it)
            }
        }
    }
}