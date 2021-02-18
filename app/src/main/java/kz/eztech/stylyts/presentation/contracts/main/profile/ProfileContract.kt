package kz.eztech.stylyts.presentation.contracts.main.profile

import kz.eztech.stylyts.domain.models.MainLentaModel
import kz.eztech.stylyts.domain.models.UserModel
import kz.eztech.stylyts.presentation.base.BasePresenter
import kz.eztech.stylyts.presentation.base.BaseView

/**
 * Created by Ruslan Erdenoff on 25.11.2020.
 */
interface ProfileContract {
    interface View : BaseView {
        fun showSettings()
        fun hideSettings()
        fun processSettings()
        fun processProfile(user:UserModel)
        fun processMyCollections(models: MainLentaModel)
    }
    interface Presenter: BasePresenter<View> {
        fun getProfile(token:String)
        fun getMyCollections(token:String,map:Map<String,Any>)
    }
}