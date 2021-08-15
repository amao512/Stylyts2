package kz.eztech.stylyts.collection_constructor.presentation.contracts

import kz.eztech.stylyts.global.domain.models.clothes.ClothesModel
import kz.eztech.stylyts.global.domain.models.clothes.ClothesCreateModel
import kz.eztech.stylyts.global.presentation.base.BasePresenter
import kz.eztech.stylyts.global.presentation.base.BaseView

/**
 * Created by Ruslan Erdenoff on 10.02.2021.
 */
interface CleanBackgroundContract {

    interface View : BaseView {

        fun processClothes(clothesModel: ClothesModel)
    }

    interface Presenter : BasePresenter<View> {

        fun saveClothes(clothesCreateModel: ClothesCreateModel)

        fun getClothesById(clothesId: Int)
    }
}