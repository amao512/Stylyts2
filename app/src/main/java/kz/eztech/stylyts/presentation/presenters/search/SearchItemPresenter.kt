package kz.eztech.stylyts.presentation.presenters.search

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import kz.eztech.stylyts.data.db.search.SearchDataSource
import kz.eztech.stylyts.data.db.search.UserSearchEntity
import kz.eztech.stylyts.data.exception.ErrorHelper
import kz.eztech.stylyts.domain.models.ResultsModel
import kz.eztech.stylyts.domain.models.clothes.ClothesModel
import kz.eztech.stylyts.domain.models.user.UserModel
import kz.eztech.stylyts.domain.usecases.search.SearchClothesUseCase
import kz.eztech.stylyts.domain.usecases.search.SearchUserUseCase
import kz.eztech.stylyts.presentation.base.processViewAction
import kz.eztech.stylyts.presentation.contracts.search.SearchItemContract
import javax.inject.Inject

/**
 * Created by Ruslan Erdenoff on 22.02.2021.
 */
class SearchItemPresenter @Inject constructor(
    private val errorHelper: ErrorHelper,
    private val searchUserUseCase: SearchUserUseCase,
    private val dataSource: SearchDataSource,
    private val searchClothesUseCase: SearchClothesUseCase
) : SearchItemContract.Presenter {

    private val disposable = CompositeDisposable()

    private lateinit var view: SearchItemContract.View

    override fun attach(view: SearchItemContract.View) {
        this.view = view
    }

    override fun disposeRequests() {
        searchUserUseCase.clear()
        searchClothesUseCase.clear()
    }

    override fun searchUserByUsername(
        token: String,
        username: String
    ) {
        searchUserUseCase.initParams(token, username)
        searchUserUseCase.execute(object : DisposableSingleObserver<ResultsModel<UserModel>>() {
            override fun onSuccess(t: ResultsModel<UserModel>) {
                view.processViewAction {
                    hideProgress()
                    processUserResults(t)
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

    override fun getUserFromLocaleDb() {
        disposable.clear()
        disposable.add(
            dataSource.allUserSearchHistory
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    view.processViewAction {
                        processUserFromLocalDb(userList = it)
                        hideProgress()
                    }
                }, {
                    view.processViewAction {
                        displayMessage(errorHelper.processError(it))
                        hideProgress()
                    }
                })
        )
    }

    override fun saveUserToLocaleDb(user: UserModel) {
        val userSearchEntity = UserSearchEntity(
            id = user.id,
            avatar = user.avatar,
            name = user.firstName,
            lastName = user.lastName,
            brand = user.isBrand,
            username = user.username
        )

        disposable.clear()
        disposable.add(
            dataSource.insertUserSearch(userSearchEntity)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe()
        )
    }

    override fun deleteUserFromLocalDb(userId: Int) {
        disposable.clear()
        disposable.add(
            dataSource.deleteUserSearch(userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe()
        )
    }

    override fun searchClothesByTitle(token: String, title: String) {
        searchClothesUseCase.initParams(token, title)
        searchClothesUseCase.execute(object : DisposableSingleObserver<ResultsModel<ClothesModel>>() {
            override fun onSuccess(t: ResultsModel<ClothesModel>) {
                view.processViewAction {
                    processClothesResults(resultsModel = t)
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