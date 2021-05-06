package kz.eztech.stylyts.data.repository

import io.reactivex.Single
import kz.eztech.stylyts.data.api.network.WardrobeApi
import kz.eztech.stylyts.data.exception.NetworkException
import kz.eztech.stylyts.data.mappers.clothes.ClothesApiModelMapper
import kz.eztech.stylyts.domain.models.clothes.ClothesModel
import kz.eztech.stylyts.domain.repository.WardrobeDomainRepository
import okhttp3.MultipartBody
import javax.inject.Inject

class WardrobeRepository @Inject constructor(
    private val api: WardrobeApi,
    private val clothesApiModelMapper: ClothesApiModelMapper
) : WardrobeDomainRepository {

    override fun createClothesByPhoto(
        token: String,
        multipartList: ArrayList<MultipartBody.Part>
    ): Single<ClothesModel> {
        return api.createClothesByPhoto(
            token = token,
            multipartList = multipartList
        ).map {
            when (it.isSuccessful) {
                true -> clothesApiModelMapper.map(data = it.body())
                false -> throw NetworkException(it)
            }
        }
    }
}