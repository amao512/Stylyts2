package kz.eztech.stylyts.presentation.contracts.profile

import kz.eztech.stylyts.domain.models.UserModel
import kz.eztech.stylyts.presentation.base.BasePresenter
import kz.eztech.stylyts.presentation.base.BaseView

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
            data: HashMap<String, Any?>
        )
    }
}