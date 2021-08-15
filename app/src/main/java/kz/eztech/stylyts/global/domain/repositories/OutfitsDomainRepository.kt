package kz.eztech.stylyts.global.domain.repositories

import io.reactivex.Single
import kz.eztech.stylyts.global.domain.models.common.ResultsModel
import kz.eztech.stylyts.global.domain.models.outfits.OutfitCreateModel
import kz.eztech.stylyts.global.domain.models.outfits.OutfitModel
import okhttp3.MultipartBody

interface OutfitsDomainRepository {

    fun saveOutfit(data: ArrayList<MultipartBody.Part>): Single<OutfitCreateModel>

    fun getOutfits(
        booleanQueryMap: Map<String, Boolean>,
        stringQueryMap: Map<String, String>
    ): Single<ResultsModel<OutfitModel>>

    fun getOutfitById(outfitId: String): Single<OutfitModel>

    fun deleteOutfit(outfitId: String): Single<Any>

    fun updateOutfit(
        outfitId: String,
        data: ArrayList<MultipartBody.Part>
    ): Single<OutfitCreateModel>
}