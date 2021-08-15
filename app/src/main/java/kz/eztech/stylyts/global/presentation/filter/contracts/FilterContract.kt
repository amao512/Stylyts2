package kz.eztech.stylyts.global.presentation.filter.contracts

import kz.eztech.stylyts.global.domain.models.clothes.ClothesFilterModel
import kz.eztech.stylyts.global.domain.models.clothes.ClothesModel
import kz.eztech.stylyts.global.domain.models.common.ResultsModel
import kz.eztech.stylyts.global.domain.models.filter.CategoryFilterSingleCheckGenre
import kz.eztech.stylyts.global.domain.models.filter.FilterCheckModel
import kz.eztech.stylyts.global.presentation.base.BasePresenter
import kz.eztech.stylyts.global.presentation.base.BaseView

interface FilterContract {

    interface View : BaseView {

        fun processWardrobe(list: List<FilterCheckModel>)

        fun processClothesCategories(list: List<CategoryFilterSingleCheckGenre>)

        fun processClothesBrands(list: List<FilterCheckModel>)

        fun processBrandCharacters(characters: List<String>)

        fun processClothesResults(resultsModel: ResultsModel<ClothesModel>)

        fun processColors(list: List<FilterCheckModel>)
    }

    interface Presenter : BasePresenter<View> {

        fun getMyWardrobe(filterModel: ClothesFilterModel)

        fun getClothesTypes()

        fun getClothesBrands(title: String)

        fun getClothesResults(filterModel: ClothesFilterModel)

        fun getColors()
    }
}