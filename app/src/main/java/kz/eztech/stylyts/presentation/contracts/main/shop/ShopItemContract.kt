package kz.eztech.stylyts.presentation.contracts.main.shop

import kz.eztech.stylyts.domain.models.ResultsModel
import kz.eztech.stylyts.domain.models.shop.ShopCategoryModel
import kz.eztech.stylyts.domain.models.clothes.ClothesTypeModel
import kz.eztech.stylyts.presentation.base.BasePresenter
import kz.eztech.stylyts.presentation.base.BaseView

/**
 * Created by Ruslan Erdenoff on 26.11.2020.
 */
interface ShopItemContract {
    interface View : BaseView {

        fun processClothesTypes(resultsModel: ResultsModel<ClothesTypeModel>)

        fun processShopCategories(shopCategoryModel: ShopCategoryModel)
    }
    interface Presenter: BasePresenter<View> {

        fun getClothesTypes(token: String)

        fun getCategory()
    }
}