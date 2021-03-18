package kz.eztech.stylyts.presentation.contracts.main.search

import kz.eztech.stylyts.domain.models.ProfileModel
import kz.eztech.stylyts.presentation.base.BasePresenter
import kz.eztech.stylyts.presentation.base.BaseView

interface SearchItemContract {

    interface View : BaseView {
        fun processUsers(list: List<ProfileModel>)
    }

    interface Presenter : BasePresenter<View> {
        fun getUserByUsername(token: String, username: String)
    }
}