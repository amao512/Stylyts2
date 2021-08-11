package kz.eztech.stylyts.data.repository

import io.reactivex.Single
import kz.eztech.stylyts.data.api.network.WardrobeApi
import kz.eztech.stylyts.data.exception.NetworkException
import kz.eztech.stylyts.utils.mappers.clothes.map
import kz.eztech.stylyts.domain.models.clothes.ClothesModel
import kz.eztech.stylyts.domain.repository.WardrobeDomainRepository
import okhttp3.MultipartBody
import javax.inject.Inject

class WardrobeRepository @Inject constructor(
    private val api: WardrobeApi
) : WardrobeDomainRepository {

    override fun createClothesByPhoto(
        multipartList: ArrayList<MultipartBody.Part>
    ): Single<ClothesModel> {
        return api.createClothesByPhoto(multipartList = multipartList).map {
            when (it.isSuccessful) {
                true -> it.body().map()
                false -> throw NetworkException(it)
            }
        }
    }
}