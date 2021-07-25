package kz.eztech.stylyts.presentation.contracts.main.shop

import kz.eztech.stylyts.domain.models.clothes.ClothesCategoryModel
import kz.eztech.stylyts.domain.models.clothes.ClothesFilterModel
import kz.eztech.stylyts.domain.models.clothes.ClothesModel
import kz.eztech.stylyts.domain.models.common.ResultsModel
import kz.eztech.stylyts.presentation.base.BasePresenter
import kz.eztech.stylyts.presentation.base.BaseView

/**
 * Created by Ruslan Erdenoff on 25.11.2020.
 */
interface ShopItemListContract {
    interface View : BaseView {

        fun processClothesResults(resultsModel: ResultsModel<ClothesModel>)

        fun processCategories(resultsModel: ResultsModel<ClothesCategoryModel>)
    }

    interface Presenter: BasePresenter<View> {

        fun getClothesResultsByType(filterModel: ClothesFilterModel)

        fun getClothesResultsByCategory(filterModel: ClothesFilterModel)

        fun getCategoriesByType(clothesTypeId: Int)
    }
}