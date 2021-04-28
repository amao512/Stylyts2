package kz.eztech.stylyts.presentation.contracts.filter

import kz.eztech.stylyts.domain.models.ResultsModel
import kz.eztech.stylyts.domain.models.clothes.ClothesCategoryModel
import kz.eztech.stylyts.domain.models.clothes.ClothesModel
import kz.eztech.stylyts.domain.models.filter.CategoryFilterSingleCheckGenre
import kz.eztech.stylyts.domain.models.filter.FilterCheckModel
import kz.eztech.stylyts.domain.models.filter.FilterModel
import kz.eztech.stylyts.presentation.base.BasePresenter
import kz.eztech.stylyts.presentation.base.BaseView

interface FilterContract {

    interface View : BaseView {

        fun processWardrobe(list: List<FilterCheckModel>)

        fun processClothesCategories(list: List<CategoryFilterSingleCheckGenre>)

        fun processClothesBrands(list: List<FilterCheckModel>)

        fun processClothesResults(resultsModel: ResultsModel<ClothesModel>)
    }

    interface Presenter : BasePresenter<View> {

        fun getMyWardrobe(
            token: String,
            filterModel: FilterModel
        )

        fun getClothesTypes(token: String)

        fun getClothesBrands(
            token: String,
            title: String
        )

        fun getClothesResults(
            token: String,
            filterModel: FilterModel
        )
    }
}