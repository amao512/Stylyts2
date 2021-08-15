package kz.eztech.stylyts.global.domain.repositories

import io.reactivex.Single
import kz.eztech.stylyts.global.domain.models.common.ResultsModel
import kz.eztech.stylyts.global.domain.models.clothes.*

interface ClothesDomainRepository {

    fun getClothesTypes(map: Map<String, String>): Single<ResultsModel<ClothesTypeModel>>

    fun getClothesStyles(map: Map<String, String>): Single<ResultsModel<ClothesStyleModel>>

    fun getClothesCategories(map: Map<String, String>): Single<ResultsModel<ClothesCategoryModel>>

    fun getClothesById(clothesId: String): Single<ClothesModel>

    fun getClothesBrands(map: Map<String, String>): Single<ResultsModel<ClothesBrandModel>>

    fun getClothesBrandById(brandId: String): Single<ClothesBrandModel>

    fun getClothesColors(map: Map<String, String>): Single<ResultsModel<ClothesColorModel>>

    fun getClothes(
        stringQueryMap: Map<String, String>,
        booleanQueryMap: Map<String, Boolean>
    ): Single<ResultsModel<ClothesModel>>

    fun saveClothesToWardrobe(clothesId: String): Single<Any>

    fun getClothesByBarcode(barcode: String): Single<ClothesModel>

    fun deleteClothes(clothesId: String): Single<Any>
}