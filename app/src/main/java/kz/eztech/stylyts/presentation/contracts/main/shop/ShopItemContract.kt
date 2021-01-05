package kz.eztech.stylyts.presentation.contracts.main.shop

import kz.eztech.stylyts.domain.models.ShopCategoryModel
import kz.eztech.stylyts.presentation.base.BasePresenter
import kz.eztech.stylyts.presentation.base.BaseView

/**
 * Created by Ruslan Erdenoff on 26.11.2020.
 */
interface ShopItemContract {
    interface View : BaseView {
        fun processShopCategories(shopCategoryModel: ShopCategoryModel)
    }
    interface Presenter: BasePresenter<View> {
        fun getCategory()
    }
}