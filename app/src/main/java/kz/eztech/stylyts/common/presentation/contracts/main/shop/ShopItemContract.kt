package kz.eztech.stylyts.common.presentation.contracts.main.shop

import kz.eztech.stylyts.collection_constructor.domain.models.ShopCategoryModel
import kz.eztech.stylyts.common.presentation.base.BasePresenter
import kz.eztech.stylyts.common.presentation.base.BaseView

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