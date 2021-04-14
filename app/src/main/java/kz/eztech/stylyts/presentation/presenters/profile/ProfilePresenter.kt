package kz.eztech.stylyts.presentation.presenters.profile

import android.app.Application
import io.reactivex.observers.DisposableSingleObserver
import kz.eztech.stylyts.R
import kz.eztech.stylyts.data.api.models.ResultsApiModel
import kz.eztech.stylyts.data.exception.ErrorHelper
import kz.eztech.stylyts.domain.models.CollectionFilterModel
import kz.eztech.stylyts.domain.models.PublicationModel
import kz.eztech.stylyts.domain.models.ResultsModel
import kz.eztech.stylyts.domain.models.clothes.ClothesModel
import kz.eztech.stylyts.domain.models.outfits.OutfitModel
import kz.eztech.stylyts.domain.models.user.FollowSuccessModel
import kz.eztech.stylyts.domain.models.user.FollowerModel
import kz.eztech.stylyts.domain.models.user.UserModel
import kz.eztech.stylyts.domain.usecases.clothes.GetClothesUseCase
import kz.eztech.stylyts.domain.usecases.outfits.GetOutfitsUseCase
import kz.eztech.stylyts.domain.usecases.profile.*
import kz.eztech.stylyts.domain.usecases.user.*
import kz.eztech.stylyts.presentation.base.processViewAction
import kz.eztech.stylyts.presentation.contracts.profile.ProfileContract
import javax.inject.Inject

/**
 * Created by Ruslan Erdenoff on 25.12.2020.
 */
class ProfilePresenter @Inject constructor(
    private val application: Application,
    private val errorHelper: ErrorHelper,
    private val getProfileUseCase: GetProfileUseCase,
    private val getUserByIdUseCase: GetUserByIdUseCase,
    private val getMyPublicationsUseCase: GetMyPublicationsUseCase,
    private val getFollowersUseCase: GetFollowersUseCase,
    private val getFollowingsUseCase: GetFollowingsUseCase,
    private val followUserUseCase: FollowUserUseCase,
    private val unfollowUserUseCase: UnfollowUserUseCase,
    private val getClothesUseCase: GetClothesUseCase,
    private val getOutfitsUseCase: GetOutfitsUseCase
) : ProfileContract.Presenter {

	private lateinit var view: ProfileContract.View

	override fun disposeRequests() {
        getProfileUseCase.clear()
		getUserByIdUseCase.clear()
		getMyPublicationsUseCase.clear()
		getFollowersUseCase.clear()
		getFollowingsUseCase.clear()
		followUserUseCase.clear()
		unfollowUserUseCase.clear()
		getOutfitsUseCase.clear()
    }

    override fun attach(view: ProfileContract.View) {
        this.view = view
    }

    override fun getProfile(
		token: String,
		userId: String,
		isOwnProfile: Boolean
	) {
        view.displayProgress()

		if (isOwnProfile) {
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
				icon = R.drawable.ic_filter
			)
        )
		filterList.add(
            CollectionFilterModel(
				id = 2,
				name = application.getString(R.string.filter_list_publishes)
			)
        )
		filterList.add(CollectionFilterModel(id = 3, name = application.getString(R.string.filter_list_photo_outfits)))

		if (isOwnProfile) {
			filterList.add(
				CollectionFilterModel(
					id = 4,
					name = application.getString(R.string.filter_list_wardrobe),
					isChosen = true
				)
			)

			filterList.add(
				CollectionFilterModel(
					id = 5,
					name = application.getString(R.string.filter_list_my_data)
				)
			)
			filterList.add(
				CollectionFilterModel(
					id = 6,
					name = application.getString(R.string.profile_add_to_wardrobe),
					icon = R.drawable.ic_baseline_add
				)
			)
		}

		view.processFilter(filterList)
	}

    override fun getPublications(
		token: String,
		isOwnProfile: Boolean
	) {
        view.displayProgress()

		if (isOwnProfile) {
			getOwnPublications(token)
		}
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

	override fun followUser(token: String, userId: Int) {
		followUserUseCase.initParams(token, userId.toString())
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

	override fun unfollowUser(token: String, userId: Int) {
		unfollowUserUseCase.initParams(token, userId.toString())
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

	override fun getWardrobe(token: String) {
		getClothesUseCase.initParams(
			token = token,
			gender = "M",
			isMyWardrobe = true
		)
		getClothesUseCase.execute(object : DisposableSingleObserver<ResultsModel<ClothesModel>>() {
			override fun onSuccess(t: ResultsModel<ClothesModel>) {
				view.processViewAction {
					processWardrobeResults(resultsModel = t)
				}
			}

			override fun onError(e: Throwable) {
				view.processViewAction {
					displayMessage(msg = errorHelper.processError(e))
				}
			}
		})
	}

	override fun getOutfits(token: String) {
		getOutfitsUseCase.initParams(token)
		getOutfitsUseCase.execute(object : DisposableSingleObserver<ResultsModel<OutfitModel>>() {
			override fun onSuccess(t: ResultsModel<OutfitModel>) {
				view.processViewAction {
					processOutfitResults(resultsModel = t)
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

	private fun getProfileById(
		token: String,
		userId: String
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

	private fun getOwnPublications(token: String) {
		getMyPublicationsUseCase.initParams(token)
		getMyPublicationsUseCase.execute(object : DisposableSingleObserver<ResultsApiModel<PublicationModel>>() {
			override fun onSuccess(t: ResultsApiModel<PublicationModel>) {
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