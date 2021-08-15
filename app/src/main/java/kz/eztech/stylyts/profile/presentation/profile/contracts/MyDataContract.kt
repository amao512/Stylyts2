package kz.eztech.stylyts.profile.presentation.profile.contracts

import kz.eztech.stylyts.global.domain.models.user.UserModel
import kz.eztech.stylyts.global.presentation.base.BasePresenter
import kz.eztech.stylyts.global.presentation.base.BaseView

interface MyDataContract {

    interface View : BaseView {

        fun processProfile(userModel: UserModel)
    }

    interface Presenter: BasePresenter<View> {

        fun getProfile()
    }
}