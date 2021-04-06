package kz.eztech.stylyts.domain.repository.clothes

import io.reactivex.Single
import kz.eztech.stylyts.domain.models.ResultsModel
import kz.eztech.stylyts.domain.models.clothes.ClothesTypeModel

interface ClothesDomainRepository {

    fun getClothesTypes(token: String): Single<ResultsModel<ClothesTypeModel>>
}