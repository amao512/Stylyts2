package kz.eztech.stylyts.domain.repository

import io.reactivex.Single
import kz.eztech.stylyts.domain.models.common.ResultsModel
import kz.eztech.stylyts.domain.models.clothes.*

interface ClothesDomainRepository {

    fun getClothesTypes(token: String): Single<ResultsModel<ClothesTypeModel>>

    fun getClothesStyles(token: String): Single<ResultsModel<ClothesStyleModel>>

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

    fun saveClothesToWardrobe(
        token: String,
        clothesId: String
    ): Single<Any>

    fun getClothesByBarcode(
        token: String,
        barcode: String
    ): Single<ClothesModel>
}