package kz.eztech.stylyts.auth.presentation.contracts

import kz.eztech.stylyts.auth.domain.models.AuthModel
import kz.eztech.stylyts.domain.models.UserModel
import kz.eztech.stylyts.common.presentation.base.BasePresenter
import kz.eztech.stylyts.common.presentation.base.BaseView

/**
 * Created by Ruslan Erdenoff on 19.11.2020.
 */
interface LoginContract {
    interface View: BaseView {

        fun processLoginUser(authModel: AuthModel)

        fun processUserProfile(userModel: UserModel)

        fun checkData()
    }

    interface Presenter: BasePresenter<View> {
        fun loginUser(data: HashMap<String, Any>)

        fun getUserProfile(token: String)
    }
}