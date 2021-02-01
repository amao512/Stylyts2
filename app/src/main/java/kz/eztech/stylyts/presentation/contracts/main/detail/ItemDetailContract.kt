package kz.eztech.stylyts.presentation.contracts.main.detail

import kz.eztech.stylyts.domain.models.ClothesMainModel
import kz.eztech.stylyts.presentation.base.BasePresenter
import kz.eztech.stylyts.presentation.base.BaseView

/**
 * Created by Ruslan Erdenoff on 04.12.2020.
 */
interface ItemDetailContract {
    interface View: BaseView {
        fun processItemDetail(model: ClothesMainModel)
    }

    interface Presenter: BasePresenter<View> {
        fun getItemDetail(token:String,id:Int)
    }
}