package kz.eztech.stylyts.profile.presentation.profile.presenters

import android.app.Application
import io.reactivex.observers.DisposableSingleObserver
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.launch
import kz.eztech.stylyts.global.data.exception.ErrorHelper
import kz.eztech.stylyts.global.domain.models.clothes.ClothesModel
import kz.eztech.stylyts.global.domain.models.common.ResultsModel
import kz.eztech.stylyts.global.domain.models.outfits.OutfitModel
import kz.eztech.stylyts.global.domain.models.posts.PostModel
import kz.eztech.stylyts.global.domain.models.user.FollowSuccessModel
import kz.eztech.stylyts.global.domain.models.user.FollowerModel
import kz.eztech.stylyts.global.domain.models.user.UserModel
import kz.eztech.stylyts.global.domain.usecases.clothes.GetClothesUseCase
import kz.eztech.stylyts.global.domain.usecases.outfits.GetOutfitsUseCase
import kz.eztech.stylyts.global.domain.usecases.posts.GetPostsUseCase
import kz.eztech.stylyts.profile.domain.usecases.profile.GetProfileUseCase
import kz.eztech.stylyts.profile.domain.usecases.profile.GetUserByIdUseCase
import kz.eztech.stylyts.global.domain.usecases.user.FollowUserUseCase
import kz.eztech.stylyts.global.domain.usecases.user.GetFollowersUseCase
import kz.eztech.stylyts.global.domain.usecases.user.UnfollowUserUseCase
import kz.eztech.stylyts.global.presentation.base.processViewAction
import kz.eztech.stylyts.profile.presentation.profile.contracts.ProfileContract
import kz.eztech.stylyts.profile.presentation.profile.data.UIProfileFilterData
import kz.eztech.stylyts.profile.presentation.profile.ui.ProfileFragment
import kz.eztech.stylyts.utils.Paginator
import javax.inject.Inject

/**
 * Created by Ruslan Erdenoff on 25.12.2020.
 */
class ProfilePresenter @Inject constructor(
    private val application: Application,
    private val errorHelper: ErrorHelper,
    private val paginator: Paginator.Store<Any>,
    private val getProfileUseCase: GetProfileUseCase,
    private val getUserByIdUseCase: GetUserByIdUseCase,
    private val getFollowersUseCase: GetFollowersUseCase,
    private val followUserUseCase: FollowUserUseCase,
    private val unfollowUserUseCase: UnfollowUserUseCase,
    private val getClothesUseCase: GetClothesUseCase,
    private val getOutfitsUseCase: GetOutfitsUseCase,
    private val getPostsUseCase: GetPostsUseCase,
    private val uiProfileFilterData: UIProfileFilterData
) : ProfileContract.Presenter, CoroutineScope by CoroutineScope(Dispatchers.Main) {

    private lateinit var view: ProfileContract.View

    init {
        launch {
            paginator.render = { view.renderPaginatorState(it) }

            paginator.sideEffects.consumeEach { effect ->
                when (effect) {
                    is Paginator.SideEffect.LoadPage -> loadPage(effect.currentPage)
                    is Paginator.SideEffect.ErrorEvent -> {
                    }
                }
            }
        }
    }

    override fun disposeRequests() {
        getProfileUseCase.clear()
        getUserByIdUseCase.clear()
        getFollowersUseCase.clear()
        followUserUseCase.clear()
        unfollowUserUseCase.clear()
        getOutfitsUseCase.clear()
        getPostsUseCase.clear()
        cancel()
    }

    override fun attach(view: ProfileContract.View) {
        this.view = view
    }

    override fun getProfile() {
        if (view.getUserId() == 0) {
            getOwnProfile()
        } else {
            getProfileById()
        }
    }

    override fun getFilerList(isOwnProfile: Boolean) {
        view.processFilter(
            uiProfileFilterData.getProfileFilter(
                context = application,
                isOwnProfile = isOwnProfile
            )
        )
    }

    override fun loadPage(page: Int) {
        when (view.getCollectionMode()) {
            ProfileFragment.POSTS_MODE -> loadPosts(page)
            ProfileFragment.WARDROBE_MODE -> loadWardrobes(page)
            ProfileFragment.OUTFITS_MODE -> loadOutfits(page)
        }
    }

    override fun loadPosts(page: Int) {
        getPostsUseCase.initParams(
            userId = view.getUserId(),
            page = page
        )
        getPostsUseCase.execute(object : DisposableSingleObserver<ResultsModel<PostModel>>() {
            override fun onSuccess(t: ResultsModel<PostModel>) {
                paginator.proceed(
                    Paginator.Action.NewPage(
                        pageNumber = t.page,
                        items = t.results
                    )
                )
            }

            override fun onError(e: Throwable) {
                paginator.proceed(Paginator.Action.PageError(e))
            }
        })
    }

    override fun loadWardrobes(page: Int) {
        getClothesUseCase.initParams(
            page = page,
            filterModel = view.getClothesFilter()
        )
        getClothesUseCase.execute(object : DisposableSingleObserver<ResultsModel<ClothesModel>>() {
            override fun onSuccess(t: ResultsModel<ClothesModel>) {
                paginator.proceed(
                    Paginator.Action.NewPage(
                        pageNumber = t.page,
                        items = t.results
                    )
                )
            }

            override fun onError(e: Throwable) {
                paginator.proceed(Paginator.Action.PageError(e))
            }
        })
    }

    override fun loadOutfits(page: Int) {
        getOutfitsUseCase.initParams(
            page = page,
            filterModel = view.getOutfitFilter()
        )
        getOutfitsUseCase.execute(object : DisposableSingleObserver<ResultsModel<OutfitModel>>() {
            override fun onSuccess(t: ResultsModel<OutfitModel>) {
                paginator.proceed(
                    Paginator.Action.NewPage(
                        pageNumber = t.page,
                        items = t.results
                    )
                )
            }

            override fun onError(e: Throwable) {
                paginator.proceed(Paginator.Action.PageError(e))
            }
        })
    }

    override fun getCollections() {
        paginator.proceed(Paginator.Action.Refresh)
    }

    override fun loadMoreList() {
        paginator.proceed(Paginator.Action.LoadMore)
    }

    override fun getFollowers() {
        getFollowersUseCase.initParams(userId = view.getUserId())
        getFollowersUseCase.execute(object :
            DisposableSingleObserver<ResultsModel<FollowerModel>>() {
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

    override fun followUser() {
        followUserUseCase.initParams(userId = view.getUserId())
        followUserUseCase.execute(object : DisposableSingleObserver<FollowSuccessModel>() {
            override fun onSuccess(t: FollowSuccessModel) {
                view.processViewAction {
                    processSuccessFollowing(followSuccessModel = t)
                }
            }

            override fun onError(e: Throwable) {
                view.processViewAction {
                    displayMessage(msg = errorHelper.processError(e))
                }
            }
        })
    }

    override fun unfollowUser() {
        unfollowUserUseCase.initParams(userId = view.getUserId())
        unfollowUserUseCase.execute(object : DisposableSingleObserver<Any>() {
            override fun onSuccess(t: Any) {
                view.processViewAction {
                    processSuccessUnfollowing()
                }
            }

            override fun onError(e: Throwable) {
                view.processViewAction {
                    processSuccessUnfollowing()
                }
            }
        })
    }

    override fun getWardrobeCount() {
        getClothesUseCase.initParams(
            filterModel = view.getClothesFilter(),
            page = 1
        )
        getClothesUseCase.execute(object : DisposableSingleObserver<ResultsModel<ClothesModel>>() {
            override fun onSuccess(t: ResultsModel<ClothesModel>) {
                view.processViewAction {
                    processWardrobeCount(count = t.totalCount)
                }
            }

            override fun onError(e: Throwable) {
                view.processViewAction {
                    displayMessage(msg = errorHelper.processError(e))
                }
            }
        })
    }

    private fun getOwnProfile() {
        getProfileUseCase.initParams()
        getProfileUseCase.execute(object : DisposableSingleObserver<UserModel>() {
            override fun onSuccess(t: UserModel) {
                view.processViewAction {
                    hideProgress()
                    processProfile(t)
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

    private fun getProfileById() {
        getUserByIdUseCase.initParams(userId = view.getUserId())
        getUserByIdUseCase.execute(object : DisposableSingleObserver<UserModel>() {
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
}