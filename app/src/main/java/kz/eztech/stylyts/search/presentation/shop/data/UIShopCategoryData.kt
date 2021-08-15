package kz.eztech.stylyts.search.presentation.shop.data

import kz.eztech.stylyts.global.domain.models.clothes.ClothesCategoryModel
import kz.eztech.stylyts.global.domain.models.clothes.ClothesTypeModel
import kz.eztech.stylyts.global.domain.models.filter.FilterCheckModel

interface UIShopCategoryData {

    fun getCategoryFilterList(
        categoryList: List<ClothesCategoryModel>,
        clothesTypeModel: ClothesTypeModel
    ): List<FilterCheckModel>
}

class UIShopCategoryDataDelegate : UIShopCategoryData {

    override fun getCategoryFilterList(
        categoryList: List<ClothesCategoryModel>,
        clothesTypeModel: ClothesTypeModel
    ): List<FilterCheckModel> {
        val preparedResults: MutableList<FilterCheckModel> = mutableListOf()

        preparedResults.add(
            FilterCheckModel(
                id = 0,
                isCustom = true,
                item = ClothesCategoryModel(
                    id = 0,
                    clothesType = clothesTypeModel,
                    title = clothesTypeModel.title,
                    bodyPart = clothesTypeModel.id
                )
            )
        )

        categoryList.map {
            preparedResults.add(
                FilterCheckModel(
                    id = it.id,
                    item = it
                )
            )
        }

        return preparedResults
    }
}