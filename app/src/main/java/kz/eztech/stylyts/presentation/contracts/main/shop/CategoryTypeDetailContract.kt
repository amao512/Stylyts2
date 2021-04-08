package kz.eztech.stylyts.presentation.contracts.main.shop

import kz.eztech.stylyts.domain.models.ResultsModel
import kz.eztech.stylyts.domain.models.clothes.ClothesModel
import kz.eztech.stylyts.presentation.base.BasePresenter
import kz.eztech.stylyts.presentation.base.BaseView

/**
 * Created by Ruslan Erdenoff on 18.12.2020.
 */
interface CategoryTypeDetailContract {
    interface View: BaseView {

        fun processTypeDetail(resultsModel: ResultsModel<ClothesModel>)
    }
    interface Presenter: BasePresenter<View> {

        fun getCategoryTypeDetail(
            token: String,
            gender: String,
            clothesCategoryId: String
        )
    }
}