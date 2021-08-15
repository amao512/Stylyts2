package kz.eztech.stylyts.profile.presentation.profile.presenters

import io.reactivex.observers.DisposableSingleObserver
import kz.eztech.stylyts.global.data.exception.ErrorHelper
import kz.eztech.stylyts.global.domain.models.user.UserModel
import kz.eztech.stylyts.profile.domain.usecases.profile.ChangeProfilePhotoUseCase
import kz.eztech.stylyts.profile.domain.usecases.profile.EditProfileUseCase
import kz.eztech.stylyts.profile.domain.usecases.profile.GetProfileUseCase
import kz.eztech.stylyts.global.presentation.base.processViewAction
import kz.eztech.stylyts.profile.presentation.profile.contracts.EditProfileContract
import kz.eztech.stylyts.utils.EMPTY_STRING
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import javax.inject.Inject

/**
 * Created by Asylzhan Seytbek on 13.03.2021.
 */
class EditProfilePresenter @Inject constructor(
    private var errorHelper: ErrorHelper,
    private val editProfileUseCase: EditProfileUseCase,
    private val getProfileUseCase: GetProfileUseCase,
    private val changeProfilePhotoUseCase: ChangeProfilePhotoUseCase
) : EditProfileContract.Presenter {

    private lateinit var view: EditProfileContract.View

    override fun attach(view: EditProfileContract.View) {
        this.view = view
    }

    override fun getProfile() {
        view.displayProgress()

        getProfileUseCase.execute(object : DisposableSingleObserver<UserModel>() {
            override fun onSuccess(t: UserModel) {
                view.processViewAction {
                    processUserModel(userModel = t)
                    hideProgress()
                }
            }

            override fun onError(e: Throwable) {
                view.processViewAction {
                    displayMessage(msg = errorHelper.processError(e))
                    hideProgress()
                }
            }
        })
    }

    override fun editProfile(data: HashMap<String, Any>) {
        view.displayProgress()

        editProfileUseCase.initParams(data)
        editProfileUseCase.execute(object : DisposableSingleObserver<UserModel>() {
            override fun onSuccess(t: UserModel) {
                view.processViewAction {
                    hideProgress()
                    processUserModel(userModel = t)
                    successEditing()
                }
            }

            override fun onError(e: Throwable) {
                view.processViewAction {
                    hideProgress()
                    displayMessage(msg = errorHelper.processError(e))
                }
            }
        })
    }

    override fun changeProfilePhoto(file: File) {
        view.displayProgress()

        val requestFile = file.asRequestBody(("image/*").toMediaTypeOrNull())

        val multipartBody = MultipartBody.Part.createFormData(
            "avatar",
            file.name,
            requestFile
        )

        changeProfilePhotoUseCase.initParams(multipartBody)
        changeProfilePhotoUseCase.execute(object : DisposableSingleObserver<UserModel>() {
            override fun onSuccess(t: UserModel) {
                view.processViewAction {
                    processUserModel(userModel = t)
                    hideProgress()
                }
            }

            override fun onError(e: Throwable) {
                view.processViewAction {
                    hideProgress()
                    displayMessage(msg = errorHelper.processError(e))
                }
            }
        })
    }

    override fun deleteProfilePhoto() {
        view.displayProgress()

        val multipartBody = MultipartBody.Part.createFormData("avatar", EMPTY_STRING)

        changeProfilePhotoUseCase.initParams(multipartBody)
        changeProfilePhotoUseCase.execute(object : DisposableSingleObserver<UserModel>() {
            override fun onSuccess(t: UserModel) {
                view.processViewAction {
                    processUserModel(userModel = t)
                    hideProgress()
                }
            }

            override fun onError(e: Throwable) {
                view.processViewAction {
                    hideProgress()
                    displayMessage(msg = errorHelper.processError(e))
                }
            }
        })
    }

    override fun disposeRequests() {
        editProfileUseCase.clear()
        getProfileUseCase.clear()
        changeProfilePhotoUseCase.clear()
    }
}