package kz.eztech.stylyts.domain.usecases.clothes

import android.util.Log
import io.reactivex.Scheduler
import io.reactivex.Single
import kz.eztech.stylyts.data.api.RestConstants
import kz.eztech.stylyts.domain.models.ResultsModel
import kz.eztech.stylyts.domain.models.clothes.ClothesModel
import kz.eztech.stylyts.domain.repository.clothes.ClothesDomainRepository
import kz.eztech.stylyts.domain.usecases.BaseUseCase
import kz.eztech.stylyts.presentation.utils.EMPTY_STRING
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
        Log.d("TAG", "$stringQueryMap")

        return clothesDomainRepository.getClothes(
            token = token,
            stringQueryMap = stringQueryMap,
            booleanQueryMap = booleanQueryMap
        )
    }

    fun initParams(
        token: String,
        gender: String = EMPTY_STRING,
        typeIdList: List<Int> = emptyList(),
        categoryIdList: List<Int> = emptyList(),
        brandIdList: List<Int> = emptyList(),
        isMyWardrobe: Boolean = false
    ) {
        this.token = RestConstants.HEADERS_AUTH_FORMAT.format(token)

        val stringQueryMap: MutableMap<String, String> = HashMap()
        val booleanQueryMap: MutableMap<String, Boolean> = HashMap()

        if (gender.isNotBlank()) {
            stringQueryMap["gender"] = gender
        }

        if (typeIdList.isNotEmpty()) {
            stringQueryMap["clothes_type"] = typeIdList.joinToString(",")
        }

        if (categoryIdList.isNotEmpty()) {
            stringQueryMap["clothes_category"] = categoryIdList.joinToString(",")
        }

        if (brandIdList.isNotEmpty()) {
            stringQueryMap["brand"] = brandIdList.joinToString(",")
        }

        if (isMyWardrobe) {
            booleanQueryMap["in_my_wardrobe"] = isMyWardrobe
        }

        this.stringQueryMap = stringQueryMap
        this.booleanQueryMap = booleanQueryMap
    }
}