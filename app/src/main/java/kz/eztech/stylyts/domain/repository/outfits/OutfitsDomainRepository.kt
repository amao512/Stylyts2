package kz.eztech.stylyts.domain.repository.outfits

import io.reactivex.Single
import kz.eztech.stylyts.domain.models.ResultsModel
import kz.eztech.stylyts.domain.models.outfits.OutfitModel

interface OutfitsDomainRepository {

    fun getOutfits(
        token: String,
        booleanQueryMap: Map<String, Boolean>
    ): Single<ResultsModel<OutfitModel>>

    fun getOutfitById(
        token: String,
        outfitId: String
    ): Single<OutfitModel>
}