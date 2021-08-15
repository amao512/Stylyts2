package kz.eztech.stylyts.profile.presentation.profile.presenters

import android.app.Application
import io.reactivex.observers.DisposableSingleObserver
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.launch
import kz.eztech.stylyts.global.data.exception.ErrorHelper
import kz.eztech.stylyts.global.domain.models.clothes.ClothesModel
import kz.eztech.stylyts.global.domain.models.clothes.ClothesTypeModel
import kz.eztech.stylyts.global.domain.models.common.ResultsModel
import kz.eztech.stylyts.global.domain.models.outfits.OutfitModel
import kz.eztech.stylyts.global.domain.models.posts.PostModel
import kz.eztech.stylyts.global.domain.models.user.FollowSuccessModel
import kz.eztech.stylyts.global.domain.models.user.FollowerModel
import kz.eztech.stylyts.global.domain.models.user.UserModel
import kz.eztech.stylyts.global.domain.usecases.clothes.GetClothesTypesUseCase
import kz.eztech.stylyts.global.domain.usecases.clothes.GetClothesUseCase
import kz.eztech.stylyts.global.domain.usecases.outfits.GetOutfitsUseCase
import kz.eztech.stylyts.global.domain.usecases.posts.GetPostsUseCase
import kz.eztech.stylyts.profile.domain.usecases.profile.GetUserByIdUseCase
import kz.eztech.stylyts.global.domain.usecases.user.FollowUserUseCase
import kz.eztech.stylyts.global.domain.usecases.user.GetFollowersUseCase
import kz.eztech.stylyts.global.domain.usecases.user.UnfollowUserUseCase
import kz.eztech.stylyts.profile.presentation.profile.contracts.ShopProfileContract
import kz.eztech.stylyts.profile.presentation.profile.ui.ShopProfileFragment
import kz.eztech.stylyts.profile.presentation.profile.data.UIShopProfileData
import kz.eztech.stylyts.utils.Paginator
import javax.inject.Inject

class ShopProfilePresenter @Inject constructor(
    private val errorHelper: ErrorHelper,
    private val paginator: Paginator.Store<Any>,
    private val getClothesTypesUseCase: GetClothesTypesUseCase,
    private val getUserByIdUseCase: GetUserByIdUseCase,
    private val getFollowersUseCase: GetFollowersUseCase,
    private val getPostsUseCase: GetPostsUseCase,
    private val getOutfitsUseCase: GetOutfitsUseCase,
    private val getClothesUseCase: GetClothesUseCase,
    private val followUserUseCase: FollowUserUseCase,
    private val unfollowUserUseCase: UnfollowUserUseCase,
    private val uiShopProfileData: UIShopProfileData,
    private val application: Application
) : ShopProfileContract.Presenter, CoroutineScope by CoroutineScope(Dispatchers.Main) {

    private lateinit var view: ShopProfileContract.View

    init {
        launch {
            paginator.render = { view.renderPaginatorState(it) }
            paginator.sideEffects.consumeEach { effect ->
                when (effect) {
                    is Paginator.SideEffect.LoadPage -> loadPage(effect.currentPage)
                    is Paginator.SideEffect.ErrorEvent -> {}
                }
            }
        }
    }

    override fun disposeRequests() {
        getUserByIdUseCase.clear()
        getFollowersUseCase.clear()
        getPostsUseCase.clear()
        getClothesTypesUseCase.clear()
        getOutfitsUseCase.clear()
        getClothesUseCase.clear()
        followUserUseCase.clear()
        unfollowUserUseCase.clear()
    }

    override fun attach(view: ShopProfileContract.View) {
        this.view = view
    }

    override fun getProfile() {
        getUserByIdUseCase.initParams(userId = view.getUserId())
        getUserByIdUseCase.execute(object : DisposableSingleObserver<UserModel>() {
            override fun onSuccess(t: UserModel) {
                view.processProfile(userModel = t)
                view.hideProgress()
            }

            override fun onError(e: Throwable) {
                view.displayMessage(msg = errorHelper.processError(e))
                view.hideProgress()
            }
        })
    }

    override fun getFollowers() {
        getFollowersUseCase.initParams(userId = view.getUserId())
        getFollowersUseCase.execute(object : DisposableSingleObserver<ResultsModel<FollowerModel>>() {
            override fun onSuccess(t: ResultsModel<FollowerModel>) {
                view.processFollowers(resultsModel = t)
            }

            override fun onError(e: Throwable) {
                view.displayMessage(msg = errorHelper.processError(e))
            }
        })
    }

    override fun getTypes() {
        getClothesTypesUseCase.initParams()
        getClothesTypesUseCase.execute(object : DisposableSingleObserver<ResultsModel<ClothesTypeModel>>() {
            override fun onSuccess(t: ResultsModel<ClothesTypeModel>) {
                view.processCollectionFilter(
                    filterList = uiShopProfileData.getProfileFilter(
                        context = application,
                        typesList = t.results
                    )
                )
            }

            override fun onError(e: Throwable) {
                view.displayMessage(msg = errorHelper.processError(e))
            }
        })
    }

    override fun loadPage(page: Int) {
        when (view.getCollectionMode()) {
            ShopProfileFragment.POSTS_MODE -> loadPostsPage(page)
            ShopProfileFragment.OUTFITS_MODE -> loadOutfitsPage(page)
            ShopProfileFragment.CLOTHES_MODE -> loadClothesPage(page)
        }
    }

    override fun loadPostsPage(page: Int) {
        getPostsUseCase.initParams(
            userId = view.getUserId(),
            page = page
        )
        getPostsUseCase.execute(object : DisposableSingleObserver<ResultsModel<PostModel>>() {
            override fun onSuccess(t: ResultsModel<PostModel>) {
                paginator.proceed(Paginator.Action.NewPage(
                    pageNumber = t.page,
                    items = t.results
                ))
            }

            override fun onError(e: Throwable) {
                paginator.proceed(Paginator.Action.PageError(error = e))
            }
        })
    }

    override fun loadOutfitsPage(page: Int) {
        getOutfitsUseCase.initParams(
            page = page,
            filterModel = view.getOutfitFilter()
        )
        getOutfitsUseCase.execute(object : DisposableSingleObserver<ResultsModel<OutfitModel>>() {
            override fun onSuccess(t: ResultsModel<OutfitModel>) {
                paginator.proceed(Paginator.Action.NewPage(
                    pageNumber = t.page,
                    items = t.results
                ))
            }

            override fun onError(e: Throwable) {
                paginator.proceed(Paginator.Action.PageError(error = e))
            }
        })
    }

    override fun loadClothesPage(page: Int) {
        getClothesUseCase.initParams(
            page = page,
            filterModel = view.getClothesFilter()
        )
        getClothesUseCase.execute(object : DisposableSingleObserver<ResultsModel<ClothesModel>>() {
            override fun onSuccess(t: ResultsModel<ClothesModel>) {
                paginator.proceed(Paginator.Action.NewPage(
                    pageNumber = t.page,
                    items = t.results
                ))
            }

            override fun onError(e: Throwable) {
                paginator.proceed(Paginator.Action.PageError(error = e))
            }
        })
    }

    override fun getCollections() {
        paginator.proceed(Paginator.Action.Refresh)
    }

    override fun loadMorePage() {
        paginator.proceed(Paginator.Action.LoadMore)
    }

    override fun onFollow() {
       followUserUseCase.initParams(userId = view.getUserId())
        followUserUseCase.execute(object : DisposableSingleObserver<FollowSuccessModel>() {
            override fun onSuccess(t: FollowSuccessModel) {
                view.processSuccessFollowing(followSuccessModel = t)
            }

            override fun onError(e: Throwable) {
                view.displayMessage(msg = errorHelper.processError(e))
            }
        })
    }

    override fun onUnFollow() {
        unfollowUserUseCase.initParams(userId = view.getUserId())
        unfollowUserUseCase.execute(object : DisposableSingleObserver<Any>() {
            override fun onSuccess(t: Any) {
                view.processSuccessUnfollowing()
            }

            override fun onError(e: Throwable) {
                view.processSuccessUnfollowing()
            }
        })
    }
}