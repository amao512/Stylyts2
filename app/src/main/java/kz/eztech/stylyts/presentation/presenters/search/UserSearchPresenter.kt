package kz.eztech.stylyts.presentation.presenters.search

import io.reactivex.observers.DisposableSingleObserver
import kz.eztech.stylyts.data.exception.ErrorHelper
import kz.eztech.stylyts.domain.models.ResultsModel
import kz.eztech.stylyts.domain.models.user.UserModel
import kz.eztech.stylyts.domain.usecases.search.SearchProfileUseCase
import kz.eztech.stylyts.presentation.base.processViewAction
import kz.eztech.stylyts.presentation.contracts.profile.UserSearchContract
import javax.inject.Inject

class UserSearchPresenter @Inject constructor(
    private val errorHelper: ErrorHelper,
    private val searchProfileUseCase: SearchProfileUseCase,
) : UserSearchContract.Presenter {

    private lateinit var view: UserSearchContract.View

    override fun disposeRequests() {
        searchProfileUseCase.clear()
    }

    override fun attach(view: UserSearchContract.View) {
        this.view = view
    }

    override fun getUserByUsername(
        token: String,
        username: String
    ) {
        searchProfileUseCase.initParams(
            token = token,
            username = username,
            isBrand = false
        )
        searchProfileUseCase.execute(object : DisposableSingleObserver<ResultsModel<UserModel>>() {
            override fun onSuccess(t: ResultsModel<UserModel>) {
                view.processViewAction {
                    processUserResults(resultsModel = t)
                }
            }

            override fun onError(e: Throwable) {
                view.processViewAction {
                    displayMessage(msg = errorHelper.processError(e))
                }
            }
        })
    }
}