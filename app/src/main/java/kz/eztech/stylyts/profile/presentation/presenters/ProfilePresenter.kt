package kz.eztech.stylyts.profile.presentation.presenters

import io.reactivex.observers.DisposableSingleObserver
import kz.eztech.stylyts.data.exception.ErrorHelper
import kz.eztech.stylyts.domain.models.MainLentaModel
import kz.eztech.stylyts.domain.models.UserModel
import kz.eztech.stylyts.domain.usecases.main.MainLentaUseCase
import kz.eztech.stylyts.profile.domain.usecases.GetProfileByIdUseCase
import kz.eztech.stylyts.profile.domain.usecases.GetProfileUseCase
import kz.eztech.stylyts.common.presentation.base.processViewAction
import kz.eztech.stylyts.profile.presentation.contracts.ProfileContract
import javax.inject.Inject

/**
 * Created by Ruslan Erdenoff on 25.12.2020.
 */
class ProfilePresenter @Inject constructor(
	private val errorHelper: ErrorHelper,
	private val getProfileUseCase: GetProfileUseCase,
	private val getProfileByIdUseCase: GetProfileByIdUseCase,
	private val myCollectionsUseCase: MainLentaUseCase
) : ProfileContract.Presenter {

	private lateinit var view: ProfileContract.View

	override fun disposeRequests() {
        getProfileUseCase.clear()
        myCollectionsUseCase.clear()
    }

    override fun attach(view: ProfileContract.View) {
        this.view = view
    }

    override fun getProfile(token: String) {
        view.displayProgress()

        getProfileUseCase.initParams(token)
        getProfileUseCase.execute(object : DisposableSingleObserver<UserModel>() {
			override fun onSuccess(t: UserModel) {
				view.processViewAction {
					hideProgress()
					processProfile(t)
//					t.id?.let {
//						val map = HashMap<String, Int>()
//						map["autor"] = it
//						getMyCollections(token, map)
//					}
				}
			}

			override fun onError(e: Throwable) {
				view.processViewAction {
					hideProgress()
					displayMessage(errorHelper.processError(e))
				}
			}
		})
    }

	override fun getProfileById(
		token: String,
		userId: String
	) {
		view.displayProgress()

		getProfileByIdUseCase.initParams(token, userId)
		getProfileByIdUseCase.execute(object : DisposableSingleObserver<UserModel>() {
			override fun onSuccess(t: UserModel) {
				view.processViewAction {
					hideProgress()
					processProfile(t)
				}
			}

			override fun onError(e: Throwable) {
				view.processViewAction {
					hideProgress()
					displayMessage(errorHelper.processError(e))
				}
			}
		})
	}

    override fun getMyCollections(
		token: String,
		map: Map<String, Any>
	) {
        view.displayProgress()

        myCollectionsUseCase.initParams(token, map)
        myCollectionsUseCase.execute(object : DisposableSingleObserver<MainLentaModel>() {
			override fun onSuccess(t: MainLentaModel) {
				view.processViewAction {
					hideProgress()
					processMyCollections(t)
				}
			}

			override fun onError(e: Throwable) {
				view.processViewAction {
					hideProgress()
					displayMessage(errorHelper.processError(e))
				}
			}
		})
    }
}