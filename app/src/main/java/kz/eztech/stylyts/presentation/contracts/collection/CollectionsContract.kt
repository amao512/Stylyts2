package kz.eztech.stylyts.presentation.contracts.collection

import kz.eztech.stylyts.domain.models.common.ResultsModel
import kz.eztech.stylyts.domain.models.clothes.ClothesStyleModel
import kz.eztech.stylyts.presentation.base.BasePresenter
import kz.eztech.stylyts.presentation.base.BaseView

/**
 * Created by Ruslan Erdenoff on 25.11.2020.
 */
interface CollectionsContract {

    interface View : BaseView {

        fun processClothesStylesResults(resultsModel: ResultsModel<ClothesStyleModel>)
    }

    interface Presenter: BasePresenter<View> {

        fun getClothesStyles(token: String)
    }
}