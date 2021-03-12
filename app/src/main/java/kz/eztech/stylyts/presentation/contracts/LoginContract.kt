package kz.eztech.stylyts.presentation.contracts

import kz.eztech.stylyts.domain.models.AuthModel
import kz.eztech.stylyts.presentation.base.BasePresenter
import kz.eztech.stylyts.presentation.base.BaseView

/**
 * Created by Ruslan Erdenoff on 19.11.2020.
 */
interface LoginContract {
    interface View: BaseView {
        fun processLoginUser(authModel: AuthModel)

        fun checkData()
    }

    interface Presenter: BasePresenter<View> {
        fun loginUser(data: HashMap<String, Any>)
    }
}