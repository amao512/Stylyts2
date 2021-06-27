package kz.eztech.stylyts.presentation.contracts.main.shop

import kz.eztech.stylyts.domain.models.clothes.ClothesBrandModel
import kz.eztech.stylyts.domain.models.clothes.ClothesFilterModel
import kz.eztech.stylyts.domain.models.common.ResultsModel
import kz.eztech.stylyts.presentation.base.BasePresenter
import kz.eztech.stylyts.presentation.base.BaseView
import kz.eztech.stylyts.presentation.utils.Paginator

/**
 * Created by Ruslan Erdenoff on 18.12.2020.
 */
interface ShopClothesListContract {

    interface View: BaseView {

        fun getToken(): String

        fun getClothesFilter(): ClothesFilterModel

        fun renderPaginatorState(state: Paginator.State)

        fun processList(list: List<Any?>)

        fun processClothes(list: List<Any>)

        fun processClothesBrands(list: List<Any>)
    }

    interface Presenter: BasePresenter<View> {

        fun loadPage(page: Int)

        fun loadBrandsPage(page: Int)

        fun getClothes()

        fun loadMorePage()

        fun getClothesBrands()

        fun loadMoreBrands()
    }
}