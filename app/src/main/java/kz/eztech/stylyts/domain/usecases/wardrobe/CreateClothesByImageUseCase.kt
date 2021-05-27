package kz.eztech.stylyts.domain.usecases.wardrobe

import io.reactivex.Scheduler
import io.reactivex.Single
import kz.eztech.stylyts.data.api.RestConstants
import kz.eztech.stylyts.domain.models.clothes.ClothesModel
import kz.eztech.stylyts.domain.models.clothes.ClothesCreateModel
import kz.eztech.stylyts.domain.repository.WardrobeDomainRepository
import kz.eztech.stylyts.domain.usecases.BaseUseCase
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

    private lateinit var token: String
    private lateinit var multipartList: ArrayList<MultipartBody.Part>

    override fun createSingleObservable(): Single<ClothesModel> {
        return wardrobeDomainRepository.createClothesByPhoto(token, multipartList)
    }

    fun initParams(
        token: String,
        clothesCreateModel: ClothesCreateModel
    ) {
        this.token = RestConstants.HEADERS_AUTH_FORMAT.format(token)

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

        clothesCreateModel.coverPhoto?.let {
            val request = it.asRequestBody(("image/*").toMediaTypeOrNull())

            multipartList.add(MultipartBody.Part.createFormData("cover_photo", it.name, request))
        }

        this.multipartList = multipartList
    }
}