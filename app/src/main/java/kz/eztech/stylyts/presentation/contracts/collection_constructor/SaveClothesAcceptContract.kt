package kz.eztech.stylyts.presentation.contracts.collection_constructor

import kz.eztech.stylyts.domain.models.ResultsModel
import kz.eztech.stylyts.domain.models.clothes.ClothesCategoryModel
import kz.eztech.stylyts.domain.models.clothes.ClothesModel
import kz.eztech.stylyts.domain.models.clothes.ClothesStyleModel
import kz.eztech.stylyts.domain.models.clothes.ClothesTypeModel
import kz.eztech.stylyts.domain.models.wardrobe.ClothesCreateModel
import kz.eztech.stylyts.presentation.base.BasePresenter
import kz.eztech.stylyts.presentation.base.BaseView

interface SaveClothesAcceptContract {

    interface View : BaseView {

        fun processTypes(resultsModel: ResultsModel<ClothesTypeModel>)

        fun processCategories(resultsModel: ResultsModel<ClothesCategoryModel>)

        fun processStyles(resultsModel: ResultsModel<ClothesStyleModel>)

        fun processSuccessCreating(wardrobeModel: ClothesModel)
    }

    interface Presenter: BasePresenter<View> {

        fun getTypes(token: String)

        fun getCategories(
            token: String,
            typeId: Int
        )

        fun getStyles(token: String)

        fun createClothes(
            token: String,
            clothesCreateModel: ClothesCreateModel
        )
    }
}