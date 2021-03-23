package kz.eztech.stylyts.profile.presentation.contracts

import kz.eztech.stylyts.common.presentation.base.BasePresenter
import kz.eztech.stylyts.common.presentation.base.BaseView

interface SettingsContract {

    interface View : BaseView {

    }

    interface Presenter : BasePresenter<View> {

    }
}