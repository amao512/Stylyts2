package kz.eztech.stylyts.search.presentation.shop.contracts

import kz.eztech.stylyts.global.domain.models.clothes.ClothesFilterModel
import kz.eztech.stylyts.global.presentation.base.BasePresenter
import kz.eztech.stylyts.global.presentation.base.BaseView
import kz.eztech.stylyts.utils.Paginator

/**
 * Created by Ruslan Erdenoff on 18.12.2020.
 */
interface ShopClothesListContract {

    interface View: BaseView {

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