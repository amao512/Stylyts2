package kz.eztech.stylyts.domain.usecases.clothes

import io.reactivex.Scheduler
import io.reactivex.Single
import kz.eztech.stylyts.data.api.RestConstants
import kz.eztech.stylyts.domain.models.ResultsModel
import kz.eztech.stylyts.domain.models.clothes.ClothesModel
import kz.eztech.stylyts.domain.models.filter.FilterModel
import kz.eztech.stylyts.domain.repository.ClothesDomainRepository
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
        filterModel: FilterModel
    ) {
        this.token = RestConstants.HEADERS_AUTH_FORMAT.format(token)

        val stringQueryMap: MutableMap<String, String> = HashMap()
        val booleanQueryMap: MutableMap<String, Boolean> = HashMap()

        if (filterModel.gender.isNotBlank()) {
            stringQueryMap["gender"] = filterModel.gender
        }

        if (filterModel.typeIdList.isNotEmpty()) {
            stringQueryMap["clothes_type"] = filterModel.typeIdList.joinToString(",")
        }

        if (filterModel.categoryIdList.isNotEmpty()) {
            stringQueryMap["clothes_category"] = filterModel.categoryIdList.joinToString(",")
        }

        if (filterModel.brandList.isNotEmpty()) {
            val brandTitleList = ArrayList<String>()

            filterModel.brandList.map {
                brandTitleList.add(it.title)
            }

            stringQueryMap["clothes_brand"] = brandTitleList.joinToString(",")
        }

        if (filterModel.isMy) {
            booleanQueryMap["in_my_wardrobe"] = filterModel.isMy
        }

        if (filterModel.page != 0) {
            stringQueryMap["page"] = filterModel.page.toString()
        }

        if (filterModel.owner.isNotBlank()) {
            stringQueryMap["owner"] = filterModel.owner
        }

        this.stringQueryMap = stringQueryMap
        this.booleanQueryMap = booleanQueryMap
    }
}