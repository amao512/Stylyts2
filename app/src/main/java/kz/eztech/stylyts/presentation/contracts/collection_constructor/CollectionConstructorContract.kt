package kz.eztech.stylyts.presentation.contracts.collection_constructor

import kz.eztech.stylyts.domain.models.ResultsModel
import kz.eztech.stylyts.domain.models.clothes.ClothesModel
import kz.eztech.stylyts.domain.models.clothes.ClothesStyleModel
import kz.eztech.stylyts.domain.models.filter.FilterModel
import kz.eztech.stylyts.presentation.base.BasePresenter

/**
 * Created by Ruslan Erdenoff on 21.12.2020.
 */
interface CollectionConstructorContract {
    interface View : MotionViewContract {

        fun processTypesResults(resultsModel: ResultsModel<kz.eztech.stylyts.domain.models.clothes.ClothesTypeModel>)

        fun processStylesResults(resultsModel: ResultsModel<ClothesStyleModel>)

        fun processClothesResults(resultsModel: ResultsModel<ClothesModel>)
    }

    interface Presenter : BasePresenter<View> {

        fun getTypes(token: String)

        fun getClothesByType(
            token: String,
            filterModel: FilterModel
        )

        fun getStyles(token: String)
    }
}