package kz.eztech.stylyts.presentation.contracts.main.constructor

import kz.eztech.stylyts.domain.models.BrandsModel
import kz.eztech.stylyts.domain.models.ShopCategoryModel
import kz.eztech.stylyts.common.presentation.base.BasePresenter
import kz.eztech.stylyts.common.presentation.base.BaseView

/**
 * Created by Ruslan Erdenoff on 05.02.2021.
 */
interface CollectionConstructorFilterContract {
    interface View: BaseView {
        fun processBrands(models: BrandsModel)
        fun clearFilter()
        fun processShopCategories(shopCategoryModel: ShopCategoryModel)
    }
    interface Presenter: BasePresenter<View> {
        fun getCategories()
        fun getBrands()
        fun getColors()
        fun getPriceRange(range:Pair<Int,Int>)
        fun getDiscount()
    }
}