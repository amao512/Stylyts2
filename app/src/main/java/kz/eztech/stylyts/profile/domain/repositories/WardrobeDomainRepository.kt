package kz.eztech.stylyts.profile.domain.repositories

import io.reactivex.Single
import kz.eztech.stylyts.global.domain.models.clothes.ClothesModel
import okhttp3.MultipartBody

interface WardrobeDomainRepository {

    fun createClothesByPhoto(multipartList: ArrayList<MultipartBody.Part>): Single<ClothesModel>
}