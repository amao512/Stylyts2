package kz.eztech.stylyts.data.repository.clothes

import io.reactivex.Single
import kz.eztech.stylyts.data.api.ClothesApi
import kz.eztech.stylyts.data.exception.NetworkException
import kz.eztech.stylyts.domain.models.ResultsModel
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
}