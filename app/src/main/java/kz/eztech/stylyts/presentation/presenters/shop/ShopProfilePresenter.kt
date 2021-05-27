package kz.eztech.stylyts.presentation.presenters.shop

import android.util.Log
import io.reactivex.observers.DisposableSingleObserver
import kz.eztech.stylyts.data.exception.ErrorHelper
import kz.eztech.stylyts.domain.models.clothes.ClothesFilterModel
import kz.eztech.stylyts.domain.models.clothes.ClothesModel
import kz.eztech.stylyts.domain.models.clothes.ClothesTypeModel
import kz.eztech.stylyts.domain.models.common.ResultsModel
import kz.eztech.stylyts.domain.models.outfits.OutfitFilterModel
import kz.eztech.stylyts.domain.models.outfits.OutfitModel
import kz.eztech.stylyts.domain.models.posts.PostFilterModel
import kz.eztech.stylyts.domain.models.posts.PostModel
import kz.eztech.stylyts.domain.models.user.FollowSuccessModel
import kz.eztech.stylyts.domain.models.user.FollowerModel
import kz.eztech.stylyts.domain.models.user.UserModel
import kz.eztech.stylyts.domain.usecases.clothes.GetClothesTypesUseCase
import kz.eztech.stylyts.domain.usecases.clothes.GetClothesUseCase
import kz.eztech.stylyts.domain.usecases.outfits.GetOutfitsUseCase
import kz.eztech.stylyts.domain.usecases.posts.GetPostsUseCase
import kz.eztech.stylyts.domain.usecases.profile.GetUserByIdUseCase
import kz.eztech.stylyts.domain.usecases.user.FollowUserUseCase
import kz.eztech.stylyts.domain.usecases.user.GetFollowersUseCase
import kz.eztech.stylyts.domain.usecases.user.UnfollowUserUseCase
import kz.eztech.stylyts.presentation.contracts.main.shop.ShopProfileContract
import javax.inject.Inject

class ShopProfilePresenter @Inject constructor(
    private val errorHelper: ErrorHelper,
    private val getClothesTypesUseCase: GetClothesTypesUseCase,
    private val getUserByIdUseCase: GetUserByIdUseCase,
    private val getFollowersUseCase: GetFollowersUseCase,
    private val getPostsUseCase: GetPostsUseCase,
    private val getOutfitsUseCase: GetOutfitsUseCase,
    private val getClothesUseCase: GetClothesUseCase,
    private val followUserUseCase: FollowUserUseCase,
    private val unfollowUserUseCase: UnfollowUserUseCase,
) : ShopProfileContract.Presenter {

    private lateinit var view: ShopProfileContract.View

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

    override fun getProfile(token: String, id: Int) {
        view.displayProgress()

        getUserByIdUseCase.initParams(token, id)
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

    override fun getFollowers(token: String, userId: Int) {
        getFollowersUseCase.initParams(token, userId)
        getFollowersUseCase.execute(object : DisposableSingleObserver<ResultsModel<FollowerModel>>() {
            override fun onSuccess(t: ResultsModel<FollowerModel>) {
                view.processFollowers(resultsModel = t)
            }

            override fun onError(e: Throwable) {
                view.displayMessage(msg = errorHelper.processError(e))
            }
        })
    }

    override fun getTypes(token: String) {
        getClothesTypesUseCase.initParams(token)
        getClothesTypesUseCase.execute(object : DisposableSingleObserver<ResultsModel<ClothesTypeModel>>() {
            override fun onSuccess(t: ResultsModel<ClothesTypeModel>) {
                view.processTypes(resultsModel = t)
            }

            override fun onError(e: Throwable) {
                view.displayMessage(msg = errorHelper.processError(e))
            }
        })
    }

    override fun getPosts(
        token: String,
        filterModel: PostFilterModel
    ) {
        getPostsUseCase.initParams(token, filterModel)
        getPostsUseCase.execute(object : DisposableSingleObserver<ResultsModel<PostModel>>() {
            override fun onSuccess(t: ResultsModel<PostModel>) {
                view.processPostResults(resultsModel = t)
            }

            override fun onError(e: Throwable) {
                view.displayMessage(msg = errorHelper.processError(e))
            }
        })
    }

    override fun getOutfits(
        token: String,
        filterModel: OutfitFilterModel
    ) {
        getOutfitsUseCase.initParams(token, filterModel)
        getOutfitsUseCase.execute(object : DisposableSingleObserver<ResultsModel<OutfitModel>>() {
            override fun onSuccess(t: ResultsModel<OutfitModel>) {
                view.processOutfits(resultsModel = t)
            }

            override fun onError(e: Throwable) {
                view.displayMessage(msg = errorHelper.processError(e))
            }
        })
    }

    override fun getClothes(
        token: String,
        filterModel: ClothesFilterModel
    ) {
        Log.d("TAG4", "$filterModel")
        getClothesUseCase.initParams(token, filterModel)
        getClothesUseCase.execute(object : DisposableSingleObserver<ResultsModel<ClothesModel>>() {
            override fun onSuccess(t: ResultsModel<ClothesModel>) {
                view.processClothes(resultsModel = t)
            }

            override fun onError(e: Throwable) {
                view.displayMessage(msg = errorHelper.processError(e))
            }
        })
    }

    override fun onFollow(
        token: String,
        userId: Int
    ) {
       followUserUseCase.initParams(token, userId)
        followUserUseCase.execute(object : DisposableSingleObserver<FollowSuccessModel>() {
            override fun onSuccess(t: FollowSuccessModel) {
                view.processSuccessFollowing(followSuccessModel = t)
            }

            override fun onError(e: Throwable) {
                view.displayMessage(msg = errorHelper.processError(e))
            }
        })
    }

    override fun onUnFollow(
        token: String,
        userId: Int
    ) {
        unfollowUserUseCase.initParams(token, userId)
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