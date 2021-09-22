package kz.eztech.stylyts.global.presentation.clothes.contracts

import kz.eztech.stylyts.global.domain.models.clothes.ClothesModel
import kz.eztech.stylyts.global.presentation.base.BasePresenter
import kz.eztech.stylyts.global.presentation.base.BaseView

/**
 * Created by Ruslan Erdenoff on 04.12.2020.
 */
interface ClothesDetailContract {

    interface View : BaseView {

        fun processClothes(clothesModel: ClothesModel)

        fun processSuccessSavedWardrobe()

        fun processInsertingCart()

        fun processDeleting()
    }

    interface Presenter : BasePresenter<View> {

        fun getClothesById(clothesId: Int)

        fun getClothesByBarcode(barcode: String)

        fun saveClothesToWardrobe(clothesId: Int)

        fun insertToCart(clothesModel: ClothesModel)

        fun deleteClothes(clothesId: Int)
    }
}