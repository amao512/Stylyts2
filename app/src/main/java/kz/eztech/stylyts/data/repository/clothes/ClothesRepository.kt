package kz.eztech.stylyts.data.repository.clothes

import io.reactivex.Single
import kz.eztech.stylyts.data.api.ClothesApi
import kz.eztech.stylyts.data.exception.NetworkException
import kz.eztech.stylyts.domain.models.ResultsModel
import kz.eztech.stylyts.domain.models.clothes.ClothesCategoryModel
import kz.eztech.stylyts.domain.models.clothes.ClothesModel
import kz.eztech.stylyts.domain.models.clothes.ClothesTypeModel
import kz.eztech.stylyts.domain.repository.clothes.ClothesDomainRepository
import javax.inject.Inject

class ClothesRepository @Inject constructor(
    private val api: ClothesApi
) : ClothesDomainRepository {

    override fun getClothesTypes(token: String): Single<ResultsModel<ClothesTypeModel>> {
        return api.getClothesTypes(token).map {
            when (it.isSuccessful) {
                true -> it.body()
                false -> throw NetworkException(it)
            }
        }
    }

    override fun getClothesCategories(token: String): Single<ResultsModel<ClothesCategoryModel>> {
        return api.getClothesCategories(token).map {
            when (it.isSuccessful) {
                true -> it.body()
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
                true -> it.body()
                false -> throw NetworkException(it)
            }
        }
    }

    override fun getClothesByType(
        token: String,
        data: HashMap<String, Any>
    ): Single<ResultsModel<ClothesModel>> {
        return api.getClothesByType(
            token = token,
            clothesTypeId = data["type_id"] as String,
            gender = data["gender_type"] as String
        ).map {
            when (it.isSuccessful) {
                true -> it.body()
                false -> throw NetworkException(it)
            }
        }
    }

    override fun getClothesByCategory(
        token: String,
        data: HashMap<String, Any>
    ): Single<ResultsModel<ClothesModel>> {
        return api.getClothesByCategory(
            token = token,
            gender = data["gender_type"] as String,
            clothesCategoryId = data["category_id"] as String
        ).map {
            when (it.isSuccessful) {
                true -> it.body()
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
                true -> it.body()
                false -> throw NetworkException(it)
            }
        }
    }
}