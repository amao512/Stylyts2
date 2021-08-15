package kz.eztech.stylyts.global.data.repositories

import io.reactivex.Single
import kz.eztech.stylyts.global.data.api.ClothesApi
import kz.eztech.stylyts.global.data.exception.NetworkException
import kz.eztech.stylyts.global.domain.models.common.ResultsModel
import kz.eztech.stylyts.global.domain.repositories.ClothesDomainRepository
import kz.eztech.stylyts.global.domain.models.clothes.*
import kz.eztech.stylyts.utils.mappers.clothes.map
import kz.eztech.stylyts.utils.mappers.map
import javax.inject.Inject

class ClothesRepository @Inject constructor(
    private val api: ClothesApi
) : ClothesDomainRepository {

    override fun getClothesTypes(map: Map<String, String>): Single<ResultsModel<ClothesTypeModel>> {
        return api.getClothesTypes(map).map {
            when (it.isSuccessful) {
                true -> it.body().map()
                false -> throw NetworkException(it)
            }
        }
    }

    override fun getClothesStyles(map: Map<String, String>): Single<ResultsModel<ClothesStyleModel>> {
        return api.getClothesStyles(map).map {
            when (it.isSuccessful) {
                true -> it.body().map()
                false -> throw NetworkException(it)
            }
        }
    }

    override fun getClothesCategories(map: Map<String, String>): Single<ResultsModel<ClothesCategoryModel>> {
        return api.getClothesCategories(map).map {
            when (it.isSuccessful) {
                true -> it.body().map()
                false -> throw NetworkException(it)
            }
        }
    }

    override fun getClothesById(clothesId: String): Single<ClothesModel> {
        return api.getClothesById(clothesId = clothesId).map {
            when (it.isSuccessful) {
                true -> it.body().map()
                false -> throw NetworkException(it)
            }
        }
    }

    override fun getClothesBrands(map: Map<String, String>): Single<ResultsModel<ClothesBrandModel>> {
        return api.getClothesBrands(map).map {
            when (it.isSuccessful) {
                true -> it.body().map()
                false -> throw NetworkException(it)
            }
        }
    }

    override fun getClothesBrandById(brandId: String): Single<ClothesBrandModel> {
        return api.getClothesBrandById(brandId = brandId).map {
            when (it.isSuccessful) {
                true -> it.body().map()
                false -> throw NetworkException(it)
            }
        }
    }

    override fun getClothesColors(map: Map<String, String>): Single<ResultsModel<ClothesColorModel>> {
        return api.getClothesColors(map).map {
            when (it.isSuccessful) {
                true -> it.body().map()
                false -> throw NetworkException(it)
            }
        }
    }

    override fun getClothes(
        stringQueryMap: Map<String, String>,
        booleanQueryMap: Map<String, Boolean>
    ): Single<ResultsModel<ClothesModel>> {
        return api.getClothes(
            stringQueryMap = stringQueryMap,
            booleanQueryMap = booleanQueryMap
        ).map {
            when (it.isSuccessful) {
                true -> it.body().map()
                false -> throw NetworkException(it)
            }
        }
    }

    override fun saveClothesToWardrobe(clothesId: String): Single<Any> {
        return api.saveClothesToWardrobe(clothesId).map {
            when (it.isSuccessful) {
                true -> it.body()
                false -> throw NetworkException(it)
            }
        }
    }

    override fun getClothesByBarcode(barcode: String): Single<ClothesModel> {
        return api.getClothesByBarcode(barcode).map {
            when (it.isSuccessful) {
                true -> it.body().map()
                false -> throw NetworkException(it)
            }
        }
    }

    override fun deleteClothes(clothesId: String): Single<Any> {
        return api.deleteClothes(clothesId).map {
            when (it.isSuccessful) {
                true -> it.body()
                false -> throw NetworkException(it)
            }
        }
    }
}