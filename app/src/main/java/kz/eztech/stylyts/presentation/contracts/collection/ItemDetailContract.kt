package kz.eztech.stylyts.presentation.contracts.collection

import kz.eztech.stylyts.domain.models.clothes.ClothesModel
import kz.eztech.stylyts.domain.models.user.UserModel
import kz.eztech.stylyts.presentation.base.BasePresenter
import kz.eztech.stylyts.presentation.base.BaseView

/**
 * Created by Ruslan Erdenoff on 04.12.2020.
 */
interface ItemDetailContract {

    interface View : BaseView {

        fun processClothes(clothesModel: ClothesModel)

        fun processClothesOwner(userModel: UserModel)

        fun processSuccessSavedWardrobe()
    }

    interface Presenter : BasePresenter<View> {

        fun getClothesById(
            token: String,
            clothesId: String
        )

        fun getClothesOwner(
            token: String,
            userId: String
        )

        fun getItemByBarcode(
            token: String,
            value: String
        )

        fun saveClothesToWardrobe(
            token: String,
            clothesId: Int
        )
    }
}