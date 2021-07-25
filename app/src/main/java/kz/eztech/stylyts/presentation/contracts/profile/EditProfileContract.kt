package kz.eztech.stylyts.presentation.contracts.profile

import kz.eztech.stylyts.domain.models.user.UserModel
import kz.eztech.stylyts.presentation.base.BasePresenter
import kz.eztech.stylyts.presentation.base.BaseView
import java.io.File

/**
 * Created by Ruslan Erdenoff on 03.03.2021.
 */
interface EditProfileContract {

    interface View : BaseView {

        fun processUserModel(userModel: UserModel)

        fun successEditing()
    }

    interface Presenter : BasePresenter<View> {

        fun getProfile()

        fun editProfile(data: HashMap<String, Any>)

        fun changeProfilePhoto(file: File)

        fun deleteProfilePhoto()
    }
}