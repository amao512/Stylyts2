package kz.eztech.stylyts.presentation.contracts.collection

import kz.eztech.stylyts.domain.models.MainLentaModel
import kz.eztech.stylyts.presentation.base.BasePresenter
import kz.eztech.stylyts.presentation.base.BaseView

/**
 * Created by Ruslan Erdenoff on 25.11.2020.
 */
interface CollectionItemContract {
    interface View : BaseView {
        fun processCollections(model: MainLentaModel)
    }
    interface Presenter: BasePresenter<View> {
        fun getCollections(token: String,map:Map<String,Any>?)
    }
}