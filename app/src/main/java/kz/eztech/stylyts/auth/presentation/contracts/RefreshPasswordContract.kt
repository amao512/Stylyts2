package kz.eztech.stylyts.auth.presentation.contracts

import kz.eztech.stylyts.common.presentation.base.BasePresenter
import kz.eztech.stylyts.common.presentation.base.BaseView
import kz.eztech.stylyts.auth.presentation.fragments.RefreshPasswordFragment

/**
 * Created by Ruslan Erdenoff on 19.11.2020.
 */
interface RefreshPasswordContract {
    interface View: BaseView {
        fun changeState(state: RefreshPasswordFragment.RefreshStates)
        fun processState()
        fun checkGenerateForgotPasswordData()
        fun checkSetNewPasswordData()
    }

    interface Presenter: BasePresenter<View> {
        fun generateForgotPassword(email:String)
        fun setNewPassword(password:String)
    }
}