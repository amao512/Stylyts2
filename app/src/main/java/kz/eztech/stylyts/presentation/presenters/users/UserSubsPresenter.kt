package kz.eztech.stylyts.presentation.presenters.users

import io.reactivex.observers.DisposableSingleObserver
import kz.eztech.stylyts.data.exception.ErrorHelper
import kz.eztech.stylyts.domain.models.ResultsModel
import kz.eztech.stylyts.domain.models.user.FollowerModel
import kz.eztech.stylyts.domain.usecases.profile.GetFollowersUseCase
import kz.eztech.stylyts.domain.usecases.profile.GetFollowingsUseCase
import kz.eztech.stylyts.presentation.base.processViewAction
import kz.eztech.stylyts.presentation.contracts.users.UserSubsContract
import javax.inject.Inject

class UserSubsPresenter @Inject constructor(
    private val errorHelper: ErrorHelper,
    private val getFollowersUseCase: GetFollowersUseCase,
    private val getFollowingsUseCase: GetFollowingsUseCase
) : UserSubsContract.Presenter {

    private lateinit var view: UserSubsContract.View

    override fun getFollowers(token: String, userId: Int) {
        getFollowersUseCase.initParams(token, userId)
        getFollowersUseCase.execute(object : DisposableSingleObserver<ResultsModel<FollowerModel>>() {
            override fun onSuccess(t: ResultsModel<FollowerModel>) {
                view.processViewAction {
                    processFollowers(resultsModel = t)
                }
            }

            override fun onError(e: Throwable) {
                view.processViewAction {
                    displayMessage(msg = errorHelper.processError(e))
                }
            }
        })
    }

    override fun getFollowings(token: String, userId: Int) {
        getFollowingsUseCase.initParams(token, userId)
        getFollowingsUseCase.execute(object : DisposableSingleObserver<ResultsModel<FollowerModel>>() {
            override fun onSuccess(t: ResultsModel<FollowerModel>) {
                view.processViewAction {
                    processFollowings(resultsModel = t)
                }
            }

            override fun onError(e: Throwable) {
                view.processViewAction {
                    displayMessage(msg = errorHelper.processError(e))
                }
            }
        })
    }

    override fun disposeRequests() {
        getFollowersUseCase.clear()
        getFollowingsUseCase.clear()
    }

    override fun attach(view: UserSubsContract.View) {
        this.view = view
    }
}