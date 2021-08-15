package kz.eztech.stylyts.collection_constructor.presentation.contracts

import kz.eztech.stylyts.global.presentation.base.BasePresenter
import kz.eztech.stylyts.global.presentation.base.BaseView
import kz.eztech.stylyts.search.domain.models.ShopCategoryModel

/**
 * Created by Ruslan Erdenoff on 05.02.2021.
 */
interface CollectionConstructorFilterContract {
    interface View : BaseView {

        fun clearFilter()

        fun processShopCategories(shopCategoryModel: ShopCategoryModel)
    }

    interface Presenter : BasePresenter<View> {

        fun getCategories()

        fun getBrands()

        fun getColors()

        fun getPriceRange(range: Pair<Int, Int>)

        fun getDiscount()
    }
}