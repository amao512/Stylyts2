package kz.eztech.stylyts.domain.usecases.clothes

import io.reactivex.Scheduler
import io.reactivex.Single
import kz.eztech.stylyts.domain.models.clothes.ClothesFilterModel
import kz.eztech.stylyts.domain.models.clothes.ClothesModel
import kz.eztech.stylyts.domain.models.common.ResultsModel
import kz.eztech.stylyts.domain.repository.ClothesDomainRepository
import kz.eztech.stylyts.domain.usecases.BaseUseCase
import javax.inject.Inject
import javax.inject.Named

class GetClothesUseCase @Inject constructor(
    @Named("executor_thread") executorThread: Scheduler,
    @Named("ui_thread") uiThread: Scheduler,
    private val clothesDomainRepository: ClothesDomainRepository
) : BaseUseCase<ResultsModel<ClothesModel>>(executorThread, uiThread) {

    private lateinit var stringQueryMap: Map<String, String>
    private lateinit var booleanQueryMap: Map<String, Boolean>

    override fun createSingleObservable(): Single<ResultsModel<ClothesModel>> {
        return clothesDomainRepository.getClothes(
            stringQueryMap = stringQueryMap,
            booleanQueryMap = booleanQueryMap
        )
    }

    fun initParams(
        page: Int = 1,
        filterModel: ClothesFilterModel
    ) {
        val stringQueryMap: MutableMap<String, String> = HashMap()
        val booleanQueryMap: MutableMap<String, Boolean> = HashMap()

        if (filterModel.gender.isNotBlank()) {
            stringQueryMap["gender"] = filterModel.gender
        }

        if (filterModel.typeIdList.isNotEmpty()) {
            stringQueryMap["clothes_type"] = filterModel.typeIdList.map { it.id }.joinToString(",")
        }

        if (filterModel.categoryIdList.isNotEmpty()) {
            stringQueryMap["clothes_category"] = filterModel.categoryIdList.map { it.id }.joinToString(",")
        }

        if (filterModel.brandList.isNotEmpty()) {
            stringQueryMap["clothes_brand"] = filterModel.brandList.joinToString(",") { it.title }
        }

        if (filterModel.inMyWardrobe) {
            booleanQueryMap["in_my_wardrobe"] = filterModel.inMyWardrobe
        }

        if (filterModel.owner.isNotBlank()) {
            stringQueryMap["owner"] = filterModel.owner
        }

        if (filterModel.minPrice != 0) {
            stringQueryMap["cost_gt"] = filterModel.minPrice.toString()
        }

        if (filterModel.maxPrice != 0) {
            stringQueryMap["cost_lt"] = filterModel.maxPrice.toString()
        }

        if (filterModel.colorList.isNotEmpty()) {
            stringQueryMap["clothes_color"] = filterModel.colorList.joinToString(",") { it.color }
        }

        stringQueryMap["page"] = page.toString()
        stringQueryMap["only_brands"] = filterModel.onlyBrands.toString()

        this.stringQueryMap = stringQueryMap
        this.booleanQueryMap = booleanQueryMap
    }
}