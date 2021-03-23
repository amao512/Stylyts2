package kz.eztech.stylyts.profile.presentation.contracts

import kz.eztech.stylyts.domain.models.ProfileModel
import kz.eztech.stylyts.common.presentation.base.BasePresenter
import kz.eztech.stylyts.common.presentation.base.BaseView

/**
 * Created by Ruslan Erdenoff on 22.02.2021.
 */
interface UserSearchContract {

    interface View : BaseView {
        fun processUser(list: List<ProfileModel>)
    }

    interface Presenter : BasePresenter<View> {
        fun getUserByUsername(token: String, username: String)
    }
}