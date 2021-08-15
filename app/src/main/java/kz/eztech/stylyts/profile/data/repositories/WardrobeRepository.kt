package kz.eztech.stylyts.profile.data.repositories

import io.reactivex.Single
import kz.eztech.stylyts.global.data.api.WardrobeApi
import kz.eztech.stylyts.global.data.exception.NetworkException
import kz.eztech.stylyts.utils.mappers.clothes.map
import kz.eztech.stylyts.global.domain.models.clothes.ClothesModel
import kz.eztech.stylyts.profile.domain.repositories.WardrobeDomainRepository
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