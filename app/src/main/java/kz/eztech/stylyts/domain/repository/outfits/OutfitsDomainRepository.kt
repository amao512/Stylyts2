package kz.eztech.stylyts.domain.repository.outfits

import io.reactivex.Single
import kz.eztech.stylyts.domain.models.MainResult
import kz.eztech.stylyts.domain.models.ResultsModel
import kz.eztech.stylyts.domain.models.outfits.OutfitModel
import okhttp3.MultipartBody

interface OutfitsDomainRepository {

    fun saveOutfit(
        token: String,
        data: ArrayList<MultipartBody.Part>
    ): Single<OutfitModel>

    fun getOutfits(
        token: String,
        booleanQueryMap: Map<String, Boolean>
    ): Single<ResultsModel<OutfitModel>>

    fun getOutfitById(
        token: String,
        outfitId: String
    ): Single<OutfitModel>

    fun deleteOutfit(
        token: String,
        outfitId: String
    ): Single<Any>
}