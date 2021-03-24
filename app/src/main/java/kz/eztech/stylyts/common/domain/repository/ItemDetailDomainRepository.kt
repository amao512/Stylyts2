package kz.eztech.stylyts.common.domain.repository

import io.reactivex.Single
import kz.eztech.stylyts.common.domain.models.ClothesMainModel
import okhttp3.MultipartBody

/**
 * Created by Ruslan Erdenoff on 28.01.2021.
 */
interface ItemDetailDomainRepository {

    fun getItemDetail(
        token: String,
        id: Int
    ): Single<ClothesMainModel>

    fun getItemByBarcode(
        token: String,
        value: String
    ): Single<ClothesMainModel>

    fun saveItemByPhoto(
        token: String,
        data: ArrayList<MultipartBody.Part>
    ): Single<ClothesMainModel>
}