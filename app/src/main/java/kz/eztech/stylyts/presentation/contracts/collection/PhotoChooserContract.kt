package kz.eztech.stylyts.presentation.contracts.collection

import android.net.Uri
import kz.eztech.stylyts.domain.models.ResultsModel
import kz.eztech.stylyts.domain.models.clothes.ClothesModel
import kz.eztech.stylyts.domain.models.clothes.ClothesTypeModel
import kz.eztech.stylyts.domain.models.filter.FilterModel
import kz.eztech.stylyts.presentation.base.BasePresenter
import kz.eztech.stylyts.presentation.contracts.collection_constructor.MotionViewContract

/**
 * Created by Ruslan Erdenoff on 23.12.2020.
 */
interface PhotoChooserContract {
    interface View : MotionViewContract {

        fun updatePhoto(path: Uri?)

        fun processTypesResults(resultsModel: ResultsModel<ClothesTypeModel>)

        fun processClothesResults(resultsModel: ResultsModel<ClothesModel>)

        fun getFilterMap(): HashMap<String, Any>
    }

    interface Presenter : BasePresenter<View> {

        fun getCategory(token: String)

        fun getClothes(
            token: String,
            filterModel: FilterModel
        )
    }
}