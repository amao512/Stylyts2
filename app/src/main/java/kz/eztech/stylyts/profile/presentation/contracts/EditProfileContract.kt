package kz.eztech.stylyts.profile.presentation.contracts

import kz.eztech.stylyts.domain.models.UserModel
import kz.eztech.stylyts.common.presentation.base.BasePresenter
import kz.eztech.stylyts.common.presentation.base.BaseView

/**
 * Created by Ruslan Erdenoff on 03.03.2021.
 */
interface EditProfileContract {

    interface View : BaseView {
        fun successEditing(userModel: UserModel)
    }

    interface Presenter : BasePresenter<View> {
        fun editProfile(
            token: String,
            data: HashMap<String, Any>
        )
    }
}