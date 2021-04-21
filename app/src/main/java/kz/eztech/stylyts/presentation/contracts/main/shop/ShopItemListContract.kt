package kz.eztech.stylyts.presentation.contracts.main.shop

import kz.eztech.stylyts.domain.models.ResultsModel
import kz.eztech.stylyts.domain.models.clothes.ClothesCategoryModel
import kz.eztech.stylyts.domain.models.clothes.ClothesModel
import kz.eztech.stylyts.domain.models.filter.FilterModel
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

        fun getClothesResultsByType(
            token: String,
            filterModel: FilterModel
        )

        fun getClothesResultsByCategory(
            token: String,
            filterModel: FilterModel
        )

        fun getCategoriesByType(
            token: String,
            clothesTypeId: Int
        )
    }
}