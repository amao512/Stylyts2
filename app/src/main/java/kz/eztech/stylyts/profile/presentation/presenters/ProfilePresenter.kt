package kz.eztech.stylyts.profile.presentation.presenters

import io.reactivex.observers.DisposableSingleObserver
import kz.eztech.stylyts.collection_constructor.domain.models.PublicationsModel
import kz.eztech.stylyts.common.data.exception.ErrorHelper
import kz.eztech.stylyts.common.domain.models.UserModel
import kz.eztech.stylyts.profile.domain.usecases.GetProfileByIdUseCase
import kz.eztech.stylyts.profile.domain.usecases.GetProfileUseCase
import kz.eztech.stylyts.common.presentation.base.processViewAction
import kz.eztech.stylyts.profile.domain.usecases.GetMyPublicationsUseCase
import kz.eztech.stylyts.profile.presentation.contracts.ProfileContract
import kz.eztech.stylyts.search.domain.models.SearchModel
import javax.inject.Inject

/**
 * Created by Ruslan Erdenoff on 25.12.2020.
 */
class ProfilePresenter @Inject constructor(
    private val errorHelper: ErrorHelper,
    private val getProfileUseCase: GetProfileUseCase,
    private val getProfileByIdUseCase: GetProfileByIdUseCase,
    private val getMyPublicationsUseCase: GetMyPublicationsUseCase
) : ProfileContract.Presenter {

	private lateinit var view: ProfileContract.View

	override fun disposeRequests() {
        getProfileUseCase.clear()
		getMyPublicationsUseCase.clear()
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

    override fun getMyPublications(token: String) {
        view.displayProgress()

		getMyPublicationsUseCase.initParams(token)
		getMyPublicationsUseCase.execute(object : DisposableSingleObserver<SearchModel<PublicationsModel>>() {
			override fun onSuccess(t: SearchModel<PublicationsModel>) {
				view.processViewAction {
					hideProgress()
					processMyPublications(t)
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