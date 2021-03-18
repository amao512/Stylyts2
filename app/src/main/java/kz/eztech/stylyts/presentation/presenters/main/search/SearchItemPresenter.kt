package kz.eztech.stylyts.presentation.presenters.main.search

import io.reactivex.observers.DisposableSingleObserver
import kz.eztech.stylyts.data.exception.ErrorHelper
import kz.eztech.stylyts.domain.models.ProfileModel
import kz.eztech.stylyts.domain.usecases.main.profile.UserSearchUseCase
import kz.eztech.stylyts.presentation.base.processViewAction
import kz.eztech.stylyts.presentation.contracts.main.search.SearchItemContract
import javax.inject.Inject

/**
 * Created by Ruslan Erdenoff on 22.02.2021.
 */
class SearchItemPresenter @Inject constructor(
    private var errorHelper: ErrorHelper,
    private var userSearchUseCase: UserSearchUseCase
) : SearchItemContract.Presenter {

    private lateinit var view: SearchItemContract.View

    override fun attach(view: SearchItemContract.View) {
        this.view = view
    }

    override fun disposeRequests() {
        userSearchUseCase.clear()
    }

    override fun getUserByUsername(
        token: String,
        username: String
    ) {
        view.displayProgress()

        userSearchUseCase.initParams(token, username)
        userSearchUseCase.execute(object : DisposableSingleObserver<List<ProfileModel>>() {
            override fun onSuccess(t: List<ProfileModel>) {
                view.processViewAction {
                    hideProgress()
                    processUsers(t)
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