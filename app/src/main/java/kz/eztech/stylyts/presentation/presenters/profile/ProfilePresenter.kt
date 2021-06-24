package kz.eztech.stylyts.presentation.presenters.profile

import android.app.Application
import io.reactivex.observers.DisposableSingleObserver
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.launch
import kz.eztech.stylyts.R
import kz.eztech.stylyts.data.exception.ErrorHelper
import kz.eztech.stylyts.domain.models.clothes.ClothesFilterModel
import kz.eztech.stylyts.domain.models.clothes.ClothesModel
import kz.eztech.stylyts.domain.models.common.ResultsModel
import kz.eztech.stylyts.domain.models.filter.CollectionFilterModel
import kz.eztech.stylyts.domain.models.outfits.OutfitModel
import kz.eztech.stylyts.domain.models.posts.PostModel
import kz.eztech.stylyts.domain.models.user.FollowSuccessModel
import kz.eztech.stylyts.domain.models.user.FollowerModel
import kz.eztech.stylyts.domain.models.user.UserModel
import kz.eztech.stylyts.domain.usecases.clothes.GetClothesUseCase
import kz.eztech.stylyts.domain.usecases.outfits.GetOutfitsUseCase
import kz.eztech.stylyts.domain.usecases.posts.GetPostsUseCase
import kz.eztech.stylyts.domain.usecases.profile.GetProfileUseCase
import kz.eztech.stylyts.domain.usecases.profile.GetUserByIdUseCase
import kz.eztech.stylyts.domain.usecases.user.FollowUserUseCase
import kz.eztech.stylyts.domain.usecases.user.GetFollowersUseCase
import kz.eztech.stylyts.domain.usecases.user.UnfollowUserUseCase
import kz.eztech.stylyts.presentation.base.processViewAction
import kz.eztech.stylyts.presentation.contracts.profile.ProfileContract
import kz.eztech.stylyts.presentation.fragments.profile.ProfileFragment
import kz.eztech.stylyts.presentation.utils.Paginator
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
	private val getPostsUseCase: GetPostsUseCase
) : ProfileContract.Presenter, CoroutineScope by CoroutineScope(Dispatchers.Main) {

	private lateinit var view: ProfileContract.View

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
        getProfileUseCase.clear()
		getUserByIdUseCase.clear()
		getFollowersUseCase.clear()
		followUserUseCase.clear()
		unfollowUserUseCase.clear()
		getOutfitsUseCase.clear()
		getPostsUseCase.clear()
    }

    override fun attach(view: ProfileContract.View) {
        this.view = view
    }

    override fun getProfile(
		token: String,
		userId: Int,
	) {
        view.displayProgress()

		if (userId == 0) {
			getOwnProfile(token)
		} else {
			getProfileById(token, userId)
		}
    }

	override fun getFilerList(isOwnProfile: Boolean) {
		val filterList = mutableListOf<CollectionFilterModel>()

		filterList.add(
            CollectionFilterModel(
				id = 1,
				name = application.getString(R.string.filter_list_filter),
				icon = R.drawable.ic_filter,
				isDisabled = true
			)
        )
		filterList.add(
            CollectionFilterModel(id = 2, name = application.getString(R.string.filter_list_publishes), isChosen = true)
        )
		filterList.add(
			CollectionFilterModel(id = 3, name = application.getString(R.string.filter_list_photo_outfits))
		)

		filterList.add(CollectionFilterModel(id = 4, name = application.getString(R.string.filter_list_wardrobe)))

		if (isOwnProfile) {
			filterList.add(CollectionFilterModel(id = 5, name = application.getString(R.string.filter_list_my_data)))
			filterList.add(
				CollectionFilterModel(
					id = 6,
					name = application.getString(R.string.profile_add_to_wardrobe_by_barcode),
					icon = R.drawable.ic_baseline_qr_code_2_24
				)
			)
			filterList.add(
				CollectionFilterModel(
					id = 7,
					name = application.getString(R.string.profile_add_to_wardrobe_by_photo),
					icon = R.drawable.ic_camera
				)
			)
		}

		view.processFilter(filterList)
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
			token = view.getToken(),
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
				paginator.proceed(Paginator.Action.PageError(e))
			}
		})
	}

	override fun loadWardrobes(page: Int) {
		getClothesUseCase.initParams(
			token = view.getToken(),
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
				paginator.proceed(Paginator.Action.PageError(e))
			}
		})
	}

	override fun loadOutfits(page: Int) {
		getOutfitsUseCase.initParams(
			token = view.getToken(),
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

	override fun followUser(
		token: String,
		userId: Int
	) {
		followUserUseCase.initParams(token, userId)
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

	override fun unfollowUser(
		token: String,
		userId: Int
	) {
		unfollowUserUseCase.initParams(token, userId)
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

	override fun getWardrobeCount(
		token: String,
		filterModel: ClothesFilterModel
	) {
		getClothesUseCase.initParams(
			token = token,
			filterModel = filterModel,
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

	private fun getOwnProfile(token: String) {
		getProfileUseCase.initParams(token)
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

	private fun getProfileById(
		token: String,
		userId: Int
	) {
		getUserByIdUseCase.initParams(token, userId)
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