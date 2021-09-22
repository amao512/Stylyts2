package kz.eztech.stylyts.global.domain.usecases.outfits

import io.reactivex.Scheduler
import io.reactivex.Single
import kz.eztech.stylyts.global.domain.models.outfits.OutfitCreateModel
import kz.eztech.stylyts.global.domain.repositories.OutfitsDomainRepository
import kz.eztech.stylyts.global.domain.usecases.BaseUseCase
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import javax.inject.Inject
import javax.inject.Named

/**
 * Created by Ruslan Erdenoff on 25.12.2020.
 */
class CreateOutfitUseCase @Inject constructor(
    @Named("executor_thread") executorThread: Scheduler,
    @Named("ui_thread") uiThread: Scheduler,
    private var outfitsDomainRepository: OutfitsDomainRepository
) : BaseUseCase<OutfitCreateModel>(executorThread, uiThread) {

    private lateinit var data: ArrayList<MultipartBody.Part>

    override fun createSingleObservable(): Single<OutfitCreateModel> {
        return outfitsDomainRepository.saveOutfit(data)
    }

    fun initParam(
        outfitCreateModel: OutfitCreateModel,
        file: File
    ) {
        val data = ArrayList<MultipartBody.Part>()

        val requestFile = file.asRequestBody(("image/*").toMediaTypeOrNull())
        val body = MultipartBody.Part.createFormData("cover_photo", file.name, requestFile)

        outfitCreateModel.clothesIdList.forEach {
            data.add(MultipartBody.Part.createFormData("clothes", it.toString()))
        }

        data.add(body)
        outfitCreateModel.itemLocation.forEachIndexed { index, clothesCollection ->
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
        data.add(MultipartBody.Part.createFormData("title", outfitCreateModel.title.toString()))
        data.add(MultipartBody.Part.createFormData("text", outfitCreateModel.text.toString()))
        data.add(MultipartBody.Part.createFormData("style", outfitCreateModel.style.toString()))
        data.add(MultipartBody.Part.createFormData("author", outfitCreateModel.authorId.toString()))
        data.add(
            MultipartBody.Part.createFormData(
                "total_price",
                outfitCreateModel.totalPrice.toString()
            )
        )

        this.data = data
    }
}