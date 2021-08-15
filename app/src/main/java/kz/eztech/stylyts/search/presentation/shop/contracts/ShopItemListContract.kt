package kz.eztech.stylyts.search.presentation.shop.contracts

import kz.eztech.stylyts.global.domain.models.clothes.ClothesFilterModel
import kz.eztech.stylyts.global.domain.models.clothes.ClothesModel
import kz.eztech.stylyts.global.domain.models.clothes.ClothesTypeModel
import kz.eztech.stylyts.global.domain.models.common.ResultsModel
import kz.eztech.stylyts.global.domain.models.filter.FilterCheckModel
import kz.eztech.stylyts.global.presentation.base.BasePresenter
import kz.eztech.stylyts.global.presentation.base.BaseView

/**
 * Created by Ruslan Erdenoff on 25.11.2020.
 */
interface ShopItemListContract {
    interface View : BaseView {

        fun processClothesResults(resultsModel: ResultsModel<ClothesModel>)

        fun processCategories(list: List<FilterCheckModel>)
    }

    interface Presenter: BasePresenter<View> {

        fun getClothesResultsByType(filterModel: ClothesFilterModel)

        fun getClothesResultsByCategory(filterModel: ClothesFilterModel)

        fun getCategoriesByType(clothesTypeModel: ClothesTypeModel)
    }
}