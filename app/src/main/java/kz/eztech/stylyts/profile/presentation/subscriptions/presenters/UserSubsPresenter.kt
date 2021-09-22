package kz.eztech.stylyts.profile.presentation.subscriptions.presenters

import io.reactivex.observers.DisposableSingleObserver
import kz.eztech.stylyts.global.data.exception.ErrorHelper
import kz.eztech.stylyts.global.domain.models.common.ResultsModel
import kz.eztech.stylyts.global.domain.models.user.FollowSuccessModel
import kz.eztech.stylyts.global.domain.models.user.FollowerModel
import kz.eztech.stylyts.global.domain.usecases.user.FollowUserUseCase
import kz.eztech.stylyts.global.domain.usecases.user.GetFollowersUseCase
import kz.eztech.stylyts.global.domain.usecases.user.GetFollowingsUseCase
import kz.eztech.stylyts.global.domain.usecases.user.UnfollowUserUseCase
import kz.eztech.stylyts.global.presentation.base.processViewAction
import kz.eztech.stylyts.profile.presentation.subscriptions.contracts.UserSubsContract
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

    override fun getFollowers(
        userId: Int,
        username: String
    ) {
        getFollowersUseCase.initParams(userId, username)
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

    override fun getFollowings(
        userId: Int,
        username: String
    ) {
        getFollowingsUseCase.initParams(userId, username)
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

    override fun followUser(followerId: Int) {
        followUserUseCase.initParams(followerId)
        followUserUseCase.execute(object : DisposableSingleObserver<FollowSuccessModel>() {
            override fun onSuccess(t: FollowSuccessModel) {
                view.processSuccessFollowing(followSuccessModel = t)
            }

            override fun onError(e: Throwable) {
                view.displayMessage(msg = errorHelper.processError(e))
            }
        })
    }

    override fun unFollowUser(followerId: Int) {
        unfollowUserUseCase.initParams(followerId)
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