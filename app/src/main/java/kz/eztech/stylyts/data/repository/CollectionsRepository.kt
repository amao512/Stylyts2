package kz.eztech.stylyts.data.repository

import io.reactivex.Single
import kz.eztech.stylyts.data.api.network.CollectionApi
import kz.eztech.stylyts.domain.repository.CollectionsDomainRepository
import kz.eztech.stylyts.data.exception.NetworkException
import kz.eztech.stylyts.domain.models.ClothesMainModel
import okhttp3.MultipartBody
import javax.inject.Inject

/**
 * Created by Ruslan Erdenoff on 28.01.2021.
 */
class CollectionsRepository @Inject constructor(
    private var api: CollectionApi
) : CollectionsDomainRepository {

    override fun getItemDetail(token: String, id: Int): Single<ClothesMainModel> {
        return api.getItemDetail(token, id).map {
            when (it.isSuccessful) {
                true -> it.body()
                else -> throw NetworkException(it)
            }
        }
    }

    override fun getItemByBarcode(token: String, value: String): Single<ClothesMainModel> {
        return api.getItemByBarcode(token, value).map {
            when (it.isSuccessful) {
                true -> it.body()
                else -> throw NetworkException(it)
            }
        }
    }

    override fun saveItemByPhoto(
        token: String,
        data: ArrayList<MultipartBody.Part>
    ): Single<ClothesMainModel> {
        return api.saveItemByPhoto(token, data).map {
            when (it.isSuccessful) {
                true -> it.body()
                else -> throw NetworkException(it)
            }
        }
    }
}