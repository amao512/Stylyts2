package kz.eztech.stylyts.ordering.presentation.cart.contracts

import kz.eztech.stylyts.ordering.data.db.cart.CartEntity
import kz.eztech.stylyts.global.domain.models.clothes.ClothesCountModel
import kz.eztech.stylyts.global.domain.models.clothes.ClothesModel
import kz.eztech.stylyts.global.domain.models.clothes.ClothesSizeModel
import kz.eztech.stylyts.global.presentation.base.BasePresenter
import kz.eztech.stylyts.global.presentation.base.BaseView

interface CartContract {

    interface View : BaseView {

        fun processCartList(list: List<CartEntity>)

        fun processSizes(
            clothesModel: ClothesModel,
            cartEntity: CartEntity,
            isSize: Boolean
        )
    }

    interface Presenter : BasePresenter<View> {

        fun getCartList()

        fun selectSize(clothesSizeModel: ClothesSizeModel)

        fun selectCount(clothesCountModel: ClothesCountModel)

        fun updateCart(cartEntity: CartEntity)

        fun removeCart(cartEntity: CartEntity)

        fun getSizes(
            clothesId: Int,
            cartEntity: CartEntity,
            isSize: Boolean = true
        )
    }
}