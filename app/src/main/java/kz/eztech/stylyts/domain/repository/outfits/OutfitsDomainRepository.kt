package kz.eztech.stylyts.domain.repository.outfits

import io.reactivex.Single
import kz.eztech.stylyts.domain.models.ResultsModel
import kz.eztech.stylyts.domain.models.outfits.OutfitModel
import okhttp3.MultipartBody
import retrofit2.http.QueryMap

interface OutfitsDomainRepository {

    fun saveOutfit(
        token: String,
        data: ArrayList<MultipartBody.Part>
    ): Single<OutfitModel>

    fun getOutfits(
        token: String,
        booleanQueryMap: Map<String, Boolean>,
        stringQueryMap: Map<String, String>
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