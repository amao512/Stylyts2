package kz.eztech.stylyts.presentation.contracts.clothes

import kz.eztech.stylyts.domain.models.clothes.ClothesModel
import kz.eztech.stylyts.presentation.base.BasePresenter
import kz.eztech.stylyts.presentation.base.BaseView

/**
 * Created by Ruslan Erdenoff on 04.12.2020.
 */
interface ClothesDetailContract {

    interface View : BaseView {

        fun processClothes(clothesModel: ClothesModel)

        fun processSuccessSavedWardrobe()

        fun processInsertingCart()
    }

    interface Presenter : BasePresenter<View> {

        fun getClothesById(
            token: String,
            clothesId: Int
        )

        fun getClothesByBarcode(
            token: String,
            barcode: String
        )

        fun saveClothesToWardrobe(
            token: String,
            clothesId: Int
        )

        fun insertToCart(clothesModel: ClothesModel)
    }
}