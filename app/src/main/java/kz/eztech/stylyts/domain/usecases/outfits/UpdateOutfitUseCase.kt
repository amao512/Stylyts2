package kz.eztech.stylyts.domain.usecases.outfits

import io.reactivex.Scheduler
import io.reactivex.Single
import kz.eztech.stylyts.data.api.RestConstants
import kz.eztech.stylyts.domain.models.outfits.OutfitCreateModel
import kz.eztech.stylyts.domain.repository.OutfitsDomainRepository
import kz.eztech.stylyts.domain.usecases.BaseUseCase
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import javax.inject.Inject
import javax.inject.Named

class UpdateOutfitUseCase @Inject constructor(
    @Named("executor_thread") executorThread: Scheduler,
    @Named("ui_thread") uiThread: Scheduler,
    private var outfitsDomainRepository: OutfitsDomainRepository
) : BaseUseCase<OutfitCreateModel>(executorThread, uiThread) {

    private lateinit var token: String
    private lateinit var outfitId: String
    private lateinit var data: ArrayList<MultipartBody.Part>

    override fun createSingleObservable(): Single<OutfitCreateModel> {
        return outfitsDomainRepository.updateOutfit(token, outfitId, data)
    }

    fun initParams(
        token: String,
        outfitId: Int,
        outfitModel: OutfitCreateModel,
        file: File
    ) {
        this.token = RestConstants.HEADERS_AUTH_FORMAT.format(token)
        this.outfitId = outfitId.toString()

        val data = ArrayList<MultipartBody.Part>()

        val requestFile = file.asRequestBody(("image/*").toMediaTypeOrNull())
        val body = MultipartBody.Part.createFormData("cover_photo", file.name, requestFile)

        outfitModel.clothes.forEach {
            data.add(MultipartBody.Part.createFormData("clothes", it.id.toString()))
        }

        data.add(body)
        outfitModel.itemLocation.forEachIndexed { index, clothesCollection ->
            data.add(
                MultipartBody.Part.createFormData(
                    "clothes_location[${index}]clothes_id",
                    clothesCollection.id.toString()
                )
            )
            data.add(
                MultipartBody.Part.createFormData(
                    "clothes_location[${index}]point_x",
                    clothesCollection.pointX.toString()
                )
            )
            data.add(
                MultipartBody.Part.createFormData(
                    "clothes_location[${index}]point_y",
                    clothesCollection.pointY.toString()
                )
            )
            data.add(
                MultipartBody.Part.createFormData(
                    "clothes_location[${index}]width",
                    clothesCollection.width.toString()
                )
            )
            data.add(
                MultipartBody.Part.createFormData(
                    "clothes_location[${index}]height",
                    clothesCollection.height.toString()
                )
            )
            data.add(
                MultipartBody.Part.createFormData(
                    "clothes_location[${index}]degree",
                    clothesCollection.degree.toString()
                )
            )
        }
        data.add(MultipartBody.Part.createFormData("title", outfitModel.title.toString()))
        data.add(MultipartBody.Part.createFormData("text", outfitModel.text.toString()))
        data.add(MultipartBody.Part.createFormData("style", outfitModel.style.toString()))
        data.add(MultipartBody.Part.createFormData("author", outfitModel.authorId.toString()))
        data.add(
            MultipartBody.Part.createFormData(
                "total_price",
                outfitModel.totalPrice.toString()
            )
        )

        this.data = data
    }
}