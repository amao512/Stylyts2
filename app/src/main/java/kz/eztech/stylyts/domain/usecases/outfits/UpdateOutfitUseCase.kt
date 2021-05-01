package kz.eztech.stylyts.domain.usecases.outfits

import io.reactivex.Scheduler
import io.reactivex.Single
import kz.eztech.stylyts.data.api.RestConstants
import kz.eztech.stylyts.domain.models.outfits.OutfitCreateModel
import kz.eztech.stylyts.domain.models.outfits.OutfitModel
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
) : BaseUseCase<OutfitModel>(executorThread, uiThread) {

    private lateinit var token: String
    private lateinit var outfitId: String
    private lateinit var data: ArrayList<MultipartBody.Part>

    override fun createSingleObservable(): Single<OutfitModel> {
        return outfitsDomainRepository.updateOutfit(token, outfitId, data)
    }

    fun initParams(
        token: String,
        outfitId: Int,
        oufitModel: OutfitCreateModel,
        file: File
    ) {
        this.token = RestConstants.HEADERS_AUTH_FORMAT.format(token)
        this.outfitId = outfitId.toString()

        val data = ArrayList<MultipartBody.Part>()

        val requestFile = file.asRequestBody(("image/*").toMediaTypeOrNull())
        val body = MultipartBody.Part.createFormData("cover_photo", file.name, requestFile)

        oufitModel.clothes.forEach {
            data.add(MultipartBody.Part.createFormData("clothes", it.toString()))
        }

        data.add(body)
        oufitModel.itemLocation.forEachIndexed { index, clothesCollection ->
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
        data.add(MultipartBody.Part.createFormData("title", oufitModel.title.toString()))
        data.add(MultipartBody.Part.createFormData("text", oufitModel.text.toString()))
        data.add(MultipartBody.Part.createFormData("style", oufitModel.style.toString()))
        data.add(MultipartBody.Part.createFormData("author", oufitModel.author.toString()))
        data.add(
            MultipartBody.Part.createFormData(
                "total_price",
                oufitModel.totalPrice.toString()
            )
        )

        this.data = data
    }
}