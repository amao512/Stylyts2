package kz.eztech.stylyts.presentation.contracts

import kz.eztech.stylyts.domain.models.UserModel
import kz.eztech.stylyts.presentation.base.BasePresenter
import kz.eztech.stylyts.presentation.base.BaseView

/**
 * Created by Ruslan Erdenoff on 19.11.2020.
 */
interface RegistrationContract {
    interface View: BaseView {
        fun checkData()
        fun processCheckedData()
        fun processSuccessRegistration()
        fun processUser(user:UserModel)
    }

    interface Presenter: BasePresenter<View> {
        fun registerUser(data:HashMap<String,Any>)
    }
}