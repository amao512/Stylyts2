package kz.eztech.stylyts.presentation.contracts.shop

import kz.eztech.stylyts.domain.models.common.SearchFilterModel
import kz.eztech.stylyts.domain.models.shop.ShopListItem
import kz.eztech.stylyts.presentation.base.BasePresenter
import kz.eztech.stylyts.presentation.base.BaseView

interface ShopListContract {

    interface View : BaseView {

        fun processShops(shopList: List<ShopListItem>)

        fun processCharacter(character: List<String>)
    }

    interface Presenter : BasePresenter<View> {

        fun getShops(
            token: String,
            currentId: Int,
            searchFilterModel: SearchFilterModel
        )

        fun searchShop(
            token: String,
            searchFilterModel: SearchFilterModel
        )
    }
}