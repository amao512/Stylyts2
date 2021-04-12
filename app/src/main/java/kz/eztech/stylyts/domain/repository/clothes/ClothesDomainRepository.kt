package kz.eztech.stylyts.domain.repository.clothes

import io.reactivex.Single
import kz.eztech.stylyts.domain.models.ResultsModel
import kz.eztech.stylyts.domain.models.clothes.ClothesBrandModel
import kz.eztech.stylyts.domain.models.clothes.ClothesCategoryModel
import kz.eztech.stylyts.domain.models.clothes.ClothesModel
import kz.eztech.stylyts.domain.models.clothes.ClothesTypeModel

interface ClothesDomainRepository {

    fun getClothesTypes(token: String): Single<ResultsModel<ClothesTypeModel>>

    fun getClothesCategories(token: String): Single<ResultsModel<ClothesCategoryModel>>

    fun getClothesCategoriesByType(
        token: String,
        typeId: String
    ): Single<ResultsModel<ClothesCategoryModel>>

    fun getClothesById(
        token: String,
        clothesId: String
    ): Single<ClothesModel>

    fun getClothesBrands(token: String): Single<ResultsModel<ClothesBrandModel>>

    fun getClothesBrandById(
        token: String,
        brandId: String
    ): Single<ClothesBrandModel>

    fun getClothes(
        token: String,
        stringQueryMap: Map<String, String>,
        booleanQueryMap: Map<String, Boolean>
    ): Single<ResultsModel<ClothesModel>>
}