package kz.eztech.stylyts.collection_constructor.domain.usecases

import io.reactivex.Scheduler
import io.reactivex.Single
import kz.eztech.stylyts.global.domain.models.clothes.ClothesCreateModel
import kz.eztech.stylyts.global.domain.models.clothes.ClothesModel
import kz.eztech.stylyts.profile.domain.repositories.WardrobeDomainRepository
import kz.eztech.stylyts.global.domain.usecases.BaseUseCase
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import javax.inject.Inject
import javax.inject.Named

class CreateClothesByImageUseCase @Inject constructor(
    @Named("executor_thread") executorThread: Scheduler,
    @Named("ui_thread") uiThread: Scheduler,
    private val wardrobeDomainRepository: WardrobeDomainRepository
) : BaseUseCase<ClothesModel>(executorThread, uiThread) {

    private lateinit var multipartList: ArrayList<MultipartBody.Part>

    override fun createSingleObservable(): Single<ClothesModel> {
        return wardrobeDomainRepository.createClothesByPhoto(multipartList)
    }

    fun initParams(clothesCreateModel: ClothesCreateModel) {
        val multipartList = ArrayList<MultipartBody.Part>()

        multipartList.add(MultipartBody.Part.createFormData("owner", clothesCreateModel.owner.toString()))
        multipartList.add(MultipartBody.Part.createFormData("title", clothesCreateModel.title))
        multipartList.add(MultipartBody.Part.createFormData("gender", clothesCreateModel.gender))
        multipartList.add(
            MultipartBody.Part.createFormData(
                "description",
                clothesCreateModel.description
            )
        )
        multipartList.add(
            MultipartBody.Part.createFormData(
                "clothes_style",
                clothesCreateModel.clothesStyle.toString()
            )
        )
        multipartList.add(
            MultipartBody.Part.createFormData(
                "clothes_category",
                clothesCreateModel.clothesCategory.toString()
            )
        )

        if (clothesCreateModel.clothesBrand != 0) {
            multipartList.add(
                MultipartBody.Part.createFormData("clothes_brand", clothesCreateModel.clothesBrand.toString())
            )
        }

        if (clothesCreateModel.cost != 0) {
            multipartList.add(
                MultipartBody.Part.createFormData("cost", clothesCreateModel.cost.toString())
            )
        }

        if (clothesCreateModel.cost != 0 && clothesCreateModel.salePrice != 0) {
            multipartList.add(
                MultipartBody.Part.createFormData("sale_price", clothesCreateModel.salePrice.toString())
            )
        }

        clothesCreateModel.coverPhoto?.let {
            val request = it.asRequestBody(("image/*").toMediaTypeOrNull())

            multipartList.add(MultipartBody.Part.createFormData("cover_photo", it.name, request))
        }

        this.multipartList = multipartList
    }
}