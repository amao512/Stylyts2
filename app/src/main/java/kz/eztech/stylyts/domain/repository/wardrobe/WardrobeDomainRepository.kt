package kz.eztech.stylyts.domain.repository.wardrobe

import io.reactivex.Single
import kz.eztech.stylyts.domain.models.wardrobe.WardrobeModel
import okhttp3.MultipartBody

interface WardrobeDomainRepository {

    fun createClothesByPhoto(
        token: String,
        multipartList: ArrayList<MultipartBody.Part>
    ): Single<WardrobeModel>
}