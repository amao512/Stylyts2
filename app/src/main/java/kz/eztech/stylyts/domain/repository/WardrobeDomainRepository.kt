package kz.eztech.stylyts.domain.repository

import io.reactivex.Single
import kz.eztech.stylyts.domain.models.clothes.ClothesModel
import okhttp3.MultipartBody

interface WardrobeDomainRepository {

    fun createClothesByPhoto(
        token: String,
        multipartList: ArrayList<MultipartBody.Part>
    ): Single<ClothesModel>
}