package kz.eztech.stylyts.domain.usecases.clothes

import io.reactivex.Scheduler
import io.reactivex.Single
import kz.eztech.stylyts.data.api.RestConstants
import kz.eztech.stylyts.domain.models.ResultsModel
import kz.eztech.stylyts.domain.models.clothes.ClothesModel
import kz.eztech.stylyts.domain.repository.clothes.ClothesDomainRepository
import kz.eztech.stylyts.domain.usecases.BaseUseCase
import javax.inject.Inject
import javax.inject.Named

class GetClothesUseCase @Inject constructor(
    @Named("executor_thread") executorThread: Scheduler,
    @Named("ui_thread") uiThread: Scheduler,
    private val clothesDomainRepository: ClothesDomainRepository
) : BaseUseCase<ResultsModel<ClothesModel>>(executorThread, uiThread) {

    private lateinit var token: String
    private lateinit var stringQueryMap: Map<String, String>
    private lateinit var booleanQueryMap: Map<String, Boolean>

    override fun createSingleObservable(): Single<ResultsModel<ClothesModel>> {
        return clothesDomainRepository.getClothes(
            token = token,
            stringQueryMap = stringQueryMap,
            booleanQueryMap = booleanQueryMap
        )
    }

    fun initParams(
        token: String,
        gender: String,
        clothesTypeId: Int = 0,
        clothesCategoryId: Int = 0,
        clothesBrandId: Int = 0,
        isMyWardrobe: Boolean = false
    ) {
        this.token = RestConstants.HEADERS_AUTH_FORMAT.format(token)

        val stringQueryMap: MutableMap<String, String> = HashMap()
        val booleanQueryMap: MutableMap<String, Boolean> = HashMap()

        stringQueryMap["gender"] = gender

        if (clothesTypeId != 0) {
            stringQueryMap["clothes_type"] = clothesTypeId.toString()
        }

        if (clothesCategoryId != 0) {
            stringQueryMap["clothes_category"] = clothesCategoryId.toString()
        }

        if (clothesBrandId != 0) {
            stringQueryMap["brand"] = clothesBrandId.toString()
        }

        if (isMyWardrobe) {
            booleanQueryMap["in_my_wardrobe"] = isMyWardrobe
        }

        this.stringQueryMap = stringQueryMap
        this.booleanQueryMap = booleanQueryMap
    }
}