package kz.eztech.stylyts.presentation.contracts.main

import kz.eztech.stylyts.domain.models.MainLentaModel
import kz.eztech.stylyts.presentation.base.BasePresenter
import kz.eztech.stylyts.presentation.base.BaseView

/**
 * Created by Ruslan Erdenoff on 20.11.2020.
 */
interface MainContract{
    interface View : BaseView{
        fun processCollections(model:MainLentaModel)
    }
    interface Presenter:BasePresenter<View>{
        fun getCollections(token: String)
    }
}