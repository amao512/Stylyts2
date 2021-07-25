package kz.eztech.stylyts.presentation.contracts.profile

import kz.eztech.stylyts.domain.models.user.UserModel
import kz.eztech.stylyts.presentation.base.BasePresenter
import kz.eztech.stylyts.presentation.base.BaseView

interface MyDataContract {

    interface View : BaseView {

        fun processProfile(userModel: UserModel)
    }

    interface Presenter: BasePresenter<View> {

        fun getProfile()
    }
}