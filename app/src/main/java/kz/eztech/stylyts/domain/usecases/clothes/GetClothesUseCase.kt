package kz.eztech.stylyts.domain.usecases.clothes

import android.util.Log
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
    private lateinit var queryMap: Map<String, String>

    override fun createSingleObservable(): Single<ResultsModel<ClothesModel>> {
        Log.d("TAG", queryMap.toString())

        return clothesDomainRepository.getClothes(token, queryMap)
    }

    fun initParams(
        token: String,
        gender: String,
        clothesTypeId: Int = 0,
        clothesCategoryId: Int = 0,
        clothesBrandId: Int = 0
    ) {
        this.token = RestConstants.HEADERS_AUTH_FORMAT.format(token)

        val queryMap: MutableMap<String, String> = HashMap()
        queryMap["gender"] = gender

        if (clothesTypeId != 0) {
            queryMap["clothes_type"] = clothesTypeId.toString()
        }

        if (clothesCategoryId != 0) {
            queryMap["clothes_category"] = clothesCategoryId.toString()
        }

        if (clothesBrandId != 0) {
            queryMap["brand"] = clothesBrandId.toString()
        }

        this.queryMap = queryMap
    }
}