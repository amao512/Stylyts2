package kz.eztech.stylyts.presentation.presenters.main.search

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import kz.eztech.stylyts.data.db.LocalDataSource
import kz.eztech.stylyts.data.db.entities.UserSearchEntity
import kz.eztech.stylyts.data.exception.ErrorHelper
import kz.eztech.stylyts.domain.models.SearchModel
import kz.eztech.stylyts.domain.models.UserModel
import kz.eztech.stylyts.domain.usecases.main.profile.UserSearchUseCase
import kz.eztech.stylyts.presentation.base.processViewAction
import kz.eztech.stylyts.presentation.contracts.main.search.SearchItemContract
import javax.inject.Inject

/**
 * Created by Ruslan Erdenoff on 22.02.2021.
 */
class SearchItemPresenter @Inject constructor(
    private val errorHelper: ErrorHelper,
    private val userSearchUseCase: UserSearchUseCase,
    private val localDataSource: LocalDataSource
) : SearchItemContract.Presenter {

    private val disposable = CompositeDisposable()

    private lateinit var view: SearchItemContract.View

    override fun attach(view: SearchItemContract.View) {
        this.view = view
    }

    override fun disposeRequests() {
        userSearchUseCase.clear()
    }

    override fun getUserByUsername(
        token: String,
        username: String
    ) {
        view.displayProgress()

        userSearchUseCase.initParams(token, username)
        userSearchUseCase.execute(object : DisposableSingleObserver<SearchModel<UserModel>>() {
            override fun onSuccess(t: SearchModel<UserModel>) {
                view.processViewAction {
                    hideProgress()
                    processSearch(t)
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
        view.displayProgress()

        disposable.clear()
        disposable.add(
            localDataSource.allUserSearchHistory
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    view.processViewAction {
                        processUserFromLocalDb(userList = it)
                    }
                }, {
                    view.processViewAction {
                        displayMessage(errorHelper.processError(it))
                    }
                })
        )
    }

    override fun saveUserToLocaleDb(user: UserModel) {
        val userSearchEntity = UserSearchEntity(
            id = user.id,
            avatar = user.avatar,
            name = user.name,
            lastName = user.lastName,
            brand = user.brand,
            username = user.username
        )

        disposable.clear()
        disposable.add(
            localDataSource.insertUserSearch(userSearchEntity)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe()
        )
    }

    override fun deleteUserFromLocalDb(userId: Int) {
        disposable.clear()
        disposable.add(
            localDataSource.deleteUserSearch(userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe()
        )
    }
}