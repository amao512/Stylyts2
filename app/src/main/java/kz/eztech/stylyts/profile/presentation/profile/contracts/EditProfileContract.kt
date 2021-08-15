package kz.eztech.stylyts.profile.presentation.profile.contracts

import kz.eztech.stylyts.global.domain.models.user.UserModel
import kz.eztech.stylyts.global.presentation.base.BasePresenter
import kz.eztech.stylyts.global.presentation.base.BaseView
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