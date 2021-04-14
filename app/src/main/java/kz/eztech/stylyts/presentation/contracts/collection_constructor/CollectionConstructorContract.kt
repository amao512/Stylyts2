package kz.eztech.stylyts.presentation.contracts.collection_constructor

import kz.eztech.stylyts.domain.models.CollectionPostCreateModel
import kz.eztech.stylyts.domain.models.ResultsModel
import kz.eztech.stylyts.domain.models.clothes.ClothesModel
import kz.eztech.stylyts.domain.models.clothes.ClothesStyleModel
import kz.eztech.stylyts.domain.models.outfits.OutfitModel
import kz.eztech.stylyts.presentation.base.BasePresenter
import java.io.File

/**
 * Created by Ruslan Erdenoff on 21.12.2020.
 */
interface CollectionConstructorContract {
    interface View : MotionViewContract {

        fun processTypesResults(resultsModel: ResultsModel<kz.eztech.stylyts.domain.models.clothes.ClothesTypeModel>)

        fun processStylesResults(resultsModel: ResultsModel<ClothesStyleModel>)

        fun processSuccessSaving(outfitModel: OutfitModel?)

        fun processClothesResults(resultsModel: ResultsModel<ClothesModel>)
    }

    interface Presenter : BasePresenter<View> {

        fun getTypes(token: String)

        fun getClothesByType(
            token: String,
            map: Map<String, Any>
        )

        fun getStyles(token: String)

        fun saveCollection(
            token: String,
            model: CollectionPostCreateModel,
            data: File
        )

        fun updateCollection(
            token: String,
            id: Int,
            model: CollectionPostCreateModel,
            data: File
        )

        fun saveCollectionToMe(
            token: String,
            id: Int
        )
    }
}