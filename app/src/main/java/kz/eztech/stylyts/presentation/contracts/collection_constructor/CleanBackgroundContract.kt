package kz.eztech.stylyts.presentation.contracts.collection_constructor

import kz.eztech.stylyts.domain.models.clothes.ClothesModel
import kz.eztech.stylyts.domain.models.wardrobe.ClothesCreateModel
import kz.eztech.stylyts.presentation.base.BasePresenter
import kz.eztech.stylyts.presentation.base.BaseView
import java.io.File

/**
 * Created by Ruslan Erdenoff on 10.02.2021.
 */
interface CleanBackgroundContract {

    interface View : BaseView {

        fun processClothes(clothesModel: ClothesModel)
    }

    interface Presenter : BasePresenter<View> {

        fun saveClothes(
            token: String,
            clothesCreateModel: ClothesCreateModel
        )

        fun getClothesById(
            token: String,
            clothesId: Int
        )
    }
}