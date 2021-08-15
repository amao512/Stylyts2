package kz.eztech.stylyts.auth.presentation.contracts

import kz.eztech.stylyts.auth.domain.models.AuthModel
import kz.eztech.stylyts.auth.data.models.ExistsUsernameModel
import kz.eztech.stylyts.global.presentation.base.BasePresenter
import kz.eztech.stylyts.global.presentation.base.BaseView

/**
 * Created by Ruslan Erdenoff on 19.11.2020.
 */
interface RegistrationContract {
    interface View : BaseView {

        fun checkData()

        fun processCheckedData()

        fun processSuccessRegistration()

        fun processUser(authModel: AuthModel)

        fun isUsernameExists(existsUsernameModel: ExistsUsernameModel)
    }

    interface Presenter : BasePresenter<View> {

        fun checkUsername(username: String)

        fun registerUser(data: HashMap<String, Any>)
    }
}