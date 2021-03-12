package kz.eztech.stylyts.presentation.presenters.main.profile

import io.reactivex.observers.DisposableSingleObserver
import kz.eztech.stylyts.data.exception.ErrorHelper
import kz.eztech.stylyts.domain.models.MainLentaModel
import kz.eztech.stylyts.domain.models.ProfileModel
import kz.eztech.stylyts.domain.usecases.main.MainLentaUseCase
import kz.eztech.stylyts.domain.usecases.main.profile.GetProfileUseCase
import kz.eztech.stylyts.presentation.base.processViewAction
import kz.eztech.stylyts.presentation.contracts.main.profile.ProfileContract
import javax.inject.Inject

/**
 * Created by Ruslan Erdenoff on 25.12.2020.
 */
class ProfilePresenter @Inject constructor(
	private var errorHelper: ErrorHelper,
	private var getProfileUseCase: GetProfileUseCase,
	private var myCollectionsUseCase: MainLentaUseCase
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
        getProfileUseCase.execute(object : DisposableSingleObserver<ProfileModel>() {
			override fun onSuccess(t: ProfileModel) {
				view.processViewAction {
					hideProgress()
					processProfile(t)
					t.owner?.let {
						val map = HashMap<String, Int>()
						map["autor"] = it
						getMyCollections(token, map)
					}

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