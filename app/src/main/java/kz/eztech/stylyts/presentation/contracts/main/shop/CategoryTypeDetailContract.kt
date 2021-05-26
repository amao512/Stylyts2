package kz.eztech.stylyts.presentation.contracts.main.shop

import kz.eztech.stylyts.domain.models.clothes.ClothesBrandModel
import kz.eztech.stylyts.domain.models.clothes.ClothesFilterModel
import kz.eztech.stylyts.domain.models.clothes.ClothesModel
import kz.eztech.stylyts.domain.models.common.ResultsModel
import kz.eztech.stylyts.presentation.base.BasePresenter
import kz.eztech.stylyts.presentation.base.BaseView

/**
 * Created by Ruslan Erdenoff on 18.12.2020.
 */
interface CategoryTypeDetailContract {
    interface View: BaseView {

        fun processClothesResults(resultsModel: ResultsModel<ClothesModel>)

        fun processClothesBrands(resultsModel: ResultsModel<ClothesBrandModel>)
    }
    interface Presenter: BasePresenter<View> {

        fun getClothesByType(
            token: String,
            filterModel: ClothesFilterModel
        )

        fun getCategoryTypeDetail(
            token: String,
            filterModel: ClothesFilterModel
        )

        fun getClothesBrands(
            token: String
        )

        fun getClothesByBrand(
            token: String,
            filterModel: ClothesFilterModel
        )
    }
}