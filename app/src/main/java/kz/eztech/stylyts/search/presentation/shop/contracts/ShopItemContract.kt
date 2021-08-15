package kz.eztech.stylyts.search.presentation.shop.contracts

import kz.eztech.stylyts.global.domain.models.clothes.ClothesTypeModel
import kz.eztech.stylyts.global.presentation.base.BasePresenter
import kz.eztech.stylyts.global.presentation.base.BaseView

/**
 * Created by Ruslan Erdenoff on 26.11.2020.
 */
interface ShopItemContract {

    interface View : BaseView {

        fun processClothesTypes(clothesTypes: List<ClothesTypeModel>)
    }
    interface Presenter: BasePresenter<View> {

        fun getClothesTypes()
    }
}