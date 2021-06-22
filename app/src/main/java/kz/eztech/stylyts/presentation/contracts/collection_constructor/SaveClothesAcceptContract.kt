package kz.eztech.stylyts.presentation.contracts.collection_constructor

import kz.eztech.stylyts.domain.models.clothes.*
import kz.eztech.stylyts.domain.models.common.ResultsModel
import kz.eztech.stylyts.presentation.base.BasePresenter
import kz.eztech.stylyts.presentation.base.BaseView

interface SaveClothesAcceptContract {

    interface View : BaseView {

        fun processTypes(resultsModel: ResultsModel<ClothesTypeModel>)

        fun processCategories(resultsModel: ResultsModel<ClothesCategoryModel>)

        fun processStyles(resultsModel: ResultsModel<ClothesStyleModel>)

        fun processBrands(resultsModel: ResultsModel<ClothesBrandModel>)

        fun processSuccessCreating(wardrobeModel: ClothesModel)

        fun displaySmallProgress()

        fun hideSmallProgress()
    }

    interface Presenter: BasePresenter<View> {

        fun getTypes(token: String)

        fun getCategories(
            token: String,
            typeId: Int
        )

        fun getStyles(token: String)

        fun getBrands(token: String)

        fun createClothes(
            token: String,
            clothesCreateModel: ClothesCreateModel
        )
    }
}