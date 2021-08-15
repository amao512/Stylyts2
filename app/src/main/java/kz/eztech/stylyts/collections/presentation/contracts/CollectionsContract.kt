package kz.eztech.stylyts.collections.presentation.contracts

import kz.eztech.stylyts.global.domain.models.common.ResultsModel
import kz.eztech.stylyts.global.domain.models.clothes.ClothesStyleModel
import kz.eztech.stylyts.global.presentation.base.BasePresenter
import kz.eztech.stylyts.global.presentation.base.BaseView

/**
 * Created by Ruslan Erdenoff on 25.11.2020.
 */
interface CollectionsContract {

    interface View : BaseView {

        fun processClothesStylesResults(resultsModel: ResultsModel<ClothesStyleModel>)
    }

    interface Presenter: BasePresenter<View> {

        fun getClothesStyles()
    }
}