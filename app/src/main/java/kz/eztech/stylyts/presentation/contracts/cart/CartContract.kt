package kz.eztech.stylyts.presentation.contracts.cart

import kz.eztech.stylyts.data.db.cart.CartEntity
import kz.eztech.stylyts.domain.models.clothes.ClothesCountModel
import kz.eztech.stylyts.domain.models.clothes.ClothesSizeModel
import kz.eztech.stylyts.presentation.base.BasePresenter
import kz.eztech.stylyts.presentation.base.BaseView

interface CartContract {

    interface View : BaseView {

        fun processCartList(list: List<CartEntity>)

        fun processSizes(
            sizesList: List<ClothesSizeModel>,
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
            token: String,
            clothesId: Int,
            cartEntity: CartEntity,
            isSize: Boolean = true
        )
    }
}