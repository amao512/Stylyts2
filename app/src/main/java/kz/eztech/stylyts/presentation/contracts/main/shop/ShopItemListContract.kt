package kz.eztech.stylyts.presentation.contracts.main.shop

import kz.eztech.stylyts.domain.models.ResultsModel
import kz.eztech.stylyts.domain.models.clothes.ClothesCategoryModel
import kz.eztech.stylyts.presentation.base.BasePresenter
import kz.eztech.stylyts.presentation.base.BaseView

/**
 * Created by Ruslan Erdenoff on 25.11.2020.
 */
interface ShopItemListContract {
    interface View : BaseView {
        fun processCategories(resultsModel: ResultsModel<ClothesCategoryModel>)
    }

    interface Presenter: BasePresenter<View> {
        fun getCategoriesByType(
            token: String,
            clothesTypeId: Int
        )
    }
}