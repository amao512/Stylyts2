package kz.eztech.stylyts.common.presentation.contracts.main.shop

import kz.eztech.stylyts.constructor.domain.models.CategoryTypeDetailModel
import kz.eztech.stylyts.common.presentation.base.BasePresenter
import kz.eztech.stylyts.common.presentation.base.BaseView

/**
 * Created by Ruslan Erdenoff on 18.12.2020.
 */
interface CategoryTypeDetailContract {
    interface View: BaseView {
        fun processTypeDetail(model: CategoryTypeDetailModel)
    }
    interface Presenter: BasePresenter<View> {
        fun getShopCategoryTypeDetail(typeId:Int,gender:String)
    }
}