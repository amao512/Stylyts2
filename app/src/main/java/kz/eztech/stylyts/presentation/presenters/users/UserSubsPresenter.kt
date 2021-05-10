package kz.eztech.stylyts.presentation.presenters.users

import io.reactivex.observers.DisposableSingleObserver
import kz.eztech.stylyts.data.exception.ErrorHelper
import kz.eztech.stylyts.domain.models.ResultsModel
import kz.eztech.stylyts.domain.models.user.FollowSuccessModel
import kz.eztech.stylyts.domain.models.user.FollowerModel
import kz.eztech.stylyts.domain.usecases.user.FollowUserUseCase
import kz.eztech.stylyts.domain.usecases.user.GetFollowersUseCase
import kz.eztech.stylyts.domain.usecases.user.GetFollowingsUseCase
import kz.eztech.stylyts.domain.usecases.user.UnfollowUserUseCase
import kz.eztech.stylyts.presentation.base.processViewAction
import kz.eztech.stylyts.presentation.contracts.users.UserSubsContract
import javax.inject.Inject

class UserSubsPresenter @Inject constructor(
    private val errorHelper: ErrorHelper,
    private val getFollowersUseCase: GetFollowersUseCase,
    private val getFollowingsUseCase: GetFollowingsUseCase,
    private val followUserUseCase: FollowUserUseCase,
    private val unfollowUserUseCase: UnfollowUserUseCase,
) : UserSubsContract.Presenter {

    private lateinit var view: UserSubsContract.View

    override fun disposeRequests() {
        getFollowersUseCase.clear()
        getFollowingsUseCase.clear()
        followUserUseCase.clear()
        unfollowUserUseCase.clear()
    }

    override fun attach(view: UserSubsContract.View) {
        this.view = view
    }

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

    override fun followUser(
        token: String,
        followerId: Int
    ) {
        followUserUseCase.initParams(token, followerId)
        followUserUseCase.execute(object : DisposableSingleObserver<FollowSuccessModel>() {
            override fun onSuccess(t: FollowSuccessModel) {
                view.processSuccessFollowing(followSuccessModel = t)
            }

            override fun onError(e: Throwable) {
                view.displayMessage(msg = errorHelper.processError(e))
            }
        })
    }

    override fun unFollowUser(
        token: String,
        followerId: Int
    ) {
        unfollowUserUseCase.initParams(token, followerId)
        unfollowUserUseCase.execute(object : DisposableSingleObserver<Any>() {
            override fun onSuccess(t: Any) {
                view.processSuccessUnFollowing(followerId = followerId)
            }

            override fun onError(e: Throwable) {
                view.processSuccessUnFollowing(followerId = followerId)
            }
        })
    }
}