package kz.eztech.stylyts.data.repository

import io.reactivex.Single
import kz.eztech.stylyts.data.api.network.ClothesApi
import kz.eztech.stylyts.data.exception.NetworkException
import kz.eztech.stylyts.data.mappers.ResultsApiModelMapper
import kz.eztech.stylyts.data.mappers.clothes.ClothesApiModelMapper
import kz.eztech.stylyts.data.mappers.clothes.ClothesBrandApiModelMapper
import kz.eztech.stylyts.domain.models.common.ResultsModel
import kz.eztech.stylyts.domain.models.clothes.*
import kz.eztech.stylyts.domain.repository.ClothesDomainRepository
import javax.inject.Inject

class ClothesRepository @Inject constructor(
    private val api: ClothesApi,
    private val resultsApiModelMapper: ResultsApiModelMapper,
    private val clothesApiModelMapper: ClothesApiModelMapper,
    private val clothesBrandApiModelMapper: ClothesBrandApiModelMapper
) : ClothesDomainRepository {

    override fun getClothesTypes(token: String): Single<ResultsModel<ClothesTypeModel>> {
        return api.getClothesTypes(token).map {
            when (it.isSuccessful) {
                true -> resultsApiModelMapper.map(data = it.body())
                false -> throw NetworkException(it)
            }
        }
    }

    override fun getClothesStyles(token: String): Single<ResultsModel<ClothesStyleModel>> {
        return api.getClothesStyles(token).map {
            when (it.isSuccessful) {
                true -> resultsApiModelMapper.map(data = it.body())
                false -> throw NetworkException(it)
            }
        }
    }

    override fun getClothesCategories(token: String): Single<ResultsModel<ClothesCategoryModel>> {
        return api.getClothesCategories(token).map {
            when (it.isSuccessful) {
                true -> resultsApiModelMapper.map(data = it.body())
                false -> throw NetworkException(it)
            }
        }
    }

    override fun getClothesCategoriesByType(
        token: String,
        typeId: String
    ): Single<ResultsModel<ClothesCategoryModel>> {
        return api.getClothesCategoriesByType(
            token = token,
            clothesTypeId = typeId
        ).map {
            when (it.isSuccessful) {
                true -> resultsApiModelMapper.map(data = it.body())
                false -> throw NetworkException(it)
            }
        }
    }

    override fun getClothesById(
        token: String,
        clothesId: String
    ): Single<ClothesModel> {
        return api.getClothesById(
            token = token,
            clothesId = clothesId
        ).map {
            when (it.isSuccessful) {
                true -> clothesApiModelMapper.map(data = it.body())
                false -> throw NetworkException(it)
            }
        }
    }

    override fun getClothesBrands(token: String): Single<ResultsModel<ClothesBrandModel>> {
        return api.getClothesBrands(token).map {
            when (it.isSuccessful) {
                true -> resultsApiModelMapper.map(data = it.body())
                false -> throw NetworkException(it)
            }
        }
    }

    override fun getClothesBrandById(
        token: String,
        brandId: String
    ): Single<ClothesBrandModel> {
        return api.getClothesBrandById(
            token = token,
            brandId = brandId
        ).map {
            when (it.isSuccessful) {
                true -> clothesBrandApiModelMapper.map(data = it.body())
                false -> throw NetworkException(it)
            }
        }
    }

    override fun getClothes(
        token: String,
        stringQueryMap: Map<String, String>,
        booleanQueryMap: Map<String, Boolean>
    ): Single<ResultsModel<ClothesModel>> {
        return api.getClothes(
            token = token,
            stringQueryMap = stringQueryMap,
            booleanQueryMap = booleanQueryMap
        ).map {
            when (it.isSuccessful) {
                true -> resultsApiModelMapper.map(data = it.body())
                false -> throw NetworkException(it)
            }
        }
    }

    override fun saveClothesToWardrobe(
        token: String,
        clothesId: String
    ): Single<Any> {
        return api.saveClothesToWardrobe(token, clothesId).map {
            when (it.isSuccessful) {
                true -> it.body()
                false -> throw NetworkException(it)
            }
        }
    }

    override fun getClothesByBarcode(
        token: String,
        barcode: String
    ): Single<ClothesModel> {
        return api.getClothesByBarcode(token, barcode).map {
            when (it.isSuccessful) {
                true -> clothesApiModelMapper.map(data = it.body())
                false -> throw NetworkException(it)
            }
        }
    }
}