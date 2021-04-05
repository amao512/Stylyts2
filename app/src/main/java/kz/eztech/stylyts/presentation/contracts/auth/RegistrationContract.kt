package kz.eztech.stylyts.presentation.contracts.auth

import kz.eztech.stylyts.domain.models.UserModel
import kz.eztech.stylyts.domain.models.auth.ExistsUsernameModel
import kz.eztech.stylyts.presentation.base.BasePresenter
import kz.eztech.stylyts.presentation.base.BaseView

/**
 * Created by Ruslan Erdenoff on 19.11.2020.
 */
interface RegistrationContract {
    interface View : BaseView {

        fun checkData()

        fun processCheckedData()

        fun processSuccessRegistration()

        fun processUser(userModel: UserModel)

        fun isUsernameExists(existsUsernameModel: ExistsUsernameModel)
    }

    interface Presenter : BasePresenter<View> {

        fun checkUsername(username: String)

        fun registerUser(data: HashMap<String, Any>)
    }
}