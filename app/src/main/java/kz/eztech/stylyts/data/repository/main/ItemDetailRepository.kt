package kz.eztech.stylyts.data.repository.main

import io.reactivex.Single
import kz.eztech.stylyts.data.api.API
import kz.eztech.stylyts.data.exception.NetworkException
import kz.eztech.stylyts.domain.models.ClothesMainModel
import kz.eztech.stylyts.domain.repository.main.ItemDetailDomainRepository
import okhttp3.MultipartBody
import javax.inject.Inject

/**
 * Created by Ruslan Erdenoff on 28.01.2021.
 */
class ItemDetailRepository:ItemDetailDomainRepository {
    private var api: API

    @Inject
    constructor(api: API){
        this.api = api
    }

    override fun getItemDetail(token: String, id: Int): Single<ClothesMainModel> {
        return api.getItemDetail(token,id).map {
            when(it.isSuccessful){
                true -> it.body()
                else ->  throw NetworkException(it)
            }
        }
    }

    override fun getItemByBarcode(token: String, value: String): Single<ClothesMainModel> {
        return api.getItemByBarcode(token,value).map {
            when(it.isSuccessful){
                true -> it.body()
                else ->  throw NetworkException(it)
            }
        }
    }

    override fun saveItemByPhoto(
        token: String,
        data: ArrayList<MultipartBody.Part>
    ): Single<ClothesMainModel> {
        return api.saveItemByPhoto(token,data).map {
            when(it.isSuccessful){
                true -> it.body()
                else ->  throw NetworkException(it)
            }
        }
    }
}