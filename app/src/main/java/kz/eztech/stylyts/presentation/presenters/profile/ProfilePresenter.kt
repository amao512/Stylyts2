package kz.eztech.stylyts.presentation.presenters.profile

import android.app.Application
import io.reactivex.observers.DisposableSingleObserver
import kz.eztech.stylyts.R
import kz.eztech.stylyts.data.api.models.ResultsApiModel
import kz.eztech.stylyts.data.exception.ErrorHelper
import kz.eztech.stylyts.domain.models.CollectionFilterModel
import kz.eztech.stylyts.domain.models.PublicationModel
import kz.eztech.stylyts.domain.models.ResultsModel
import kz.eztech.stylyts.domain.models.user.FollowerModel
import kz.eztech.stylyts.domain.models.user.UserModel
import kz.eztech.stylyts.domain.usecases.profile.*
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
	private val getProfileByIdUseCase: GetProfileByIdUseCase,
	private val getMyPublicationsUseCase: GetMyPublicationsUseCase,
	private val getFollowersUseCase: GetFollowersUseCase,
	private val getFollowingsUseCase: GetFollowingsUseCase
) : ProfileContract.Presenter {

	private lateinit var view: ProfileContract.View

	override fun disposeRequests() {
        getProfileUseCase.clear()
		getProfileByIdUseCase.clear()
		getMyPublicationsUseCase.clear()
		getFollowersUseCase.clear()
		getFollowingsUseCase.clear()
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
				name = application.getString(R.string.filter_list_publishes),
				isChosen = true
			)
        )
		filterList.add(CollectionFilterModel(id = 3, name = application.getString(R.string.filter_list_photo_outfits)))
		filterList.add(CollectionFilterModel(id = 4, name = application.getString(R.string.filter_list_wardrobe)))

		if (isOwnProfile) {
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
		getProfileByIdUseCase.initParams(token, userId)
		getProfileByIdUseCase.execute(object : DisposableSingleObserver<UserModel>() {
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