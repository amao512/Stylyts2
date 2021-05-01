package kz.eztech.stylyts.data.repository

import io.reactivex.Single
import kz.eztech.stylyts.data.api.network.WardrobeApi
import kz.eztech.stylyts.data.exception.NetworkException
import kz.eztech.stylyts.data.mappers.wardrobe.WardrobeApiModelMapper
import kz.eztech.stylyts.domain.models.wardrobe.WardrobeModel
import kz.eztech.stylyts.domain.repository.WardrobeDomainRepository
import okhttp3.MultipartBody
import javax.inject.Inject

class WardrobeRepository @Inject constructor(
    private val api: WardrobeApi,
    private val wardrobeApiModelMapper: WardrobeApiModelMapper
) : WardrobeDomainRepository {

    override fun createClothesByPhoto(
        token: String,
        multipartList: ArrayList<MultipartBody.Part>
    ): Single<WardrobeModel> {
        return api.createClothesByPhoto(
            token = token,
            multipartList = multipartList
        ).map {
            when (it.isSuccessful) {
                true -> wardrobeApiModelMapper.map(data = it.body())
                false -> throw NetworkException(it)
            }
        }
    }
}