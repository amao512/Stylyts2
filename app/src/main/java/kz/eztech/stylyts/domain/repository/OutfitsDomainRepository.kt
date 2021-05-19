package kz.eztech.stylyts.domain.repository

import io.reactivex.Single
import kz.eztech.stylyts.domain.models.common.ResultsModel
import kz.eztech.stylyts.domain.models.outfits.OutfitCreateModel
import kz.eztech.stylyts.domain.models.outfits.OutfitModel
import okhttp3.MultipartBody

interface OutfitsDomainRepository {

    fun saveOutfit(
        token: String,
        data: ArrayList<MultipartBody.Part>
    ): Single<OutfitCreateModel>

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

    fun updateOutfit(
        token: String,
        outfitId: String,
        data: ArrayList<MultipartBody.Part>
    ): Single<OutfitCreateModel>
}