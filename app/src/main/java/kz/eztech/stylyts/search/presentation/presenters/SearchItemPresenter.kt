package kz.eztech.stylyts.search.presentation.presenters

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import kz.eztech.stylyts.common.data.exception.ErrorHelper
import kz.eztech.stylyts.common.domain.models.UserModel
import kz.eztech.stylyts.common.presentation.base.processViewAction
import kz.eztech.stylyts.search.data.db.SearchDataSource
import kz.eztech.stylyts.search.data.db.UserSearchEntity
import kz.eztech.stylyts.search.domain.models.SearchModel
import kz.eztech.stylyts.search.domain.usecases.SearchUserUseCase
import kz.eztech.stylyts.search.presentation.contracts.SearchItemContract
import javax.inject.Inject

/**
 * Created by Ruslan Erdenoff on 22.02.2021.
 */
class SearchItemPresenter @Inject constructor(
    private val errorHelper: ErrorHelper,
    private val searchUserUseCase: SearchUserUseCase,
    private val dataSource: SearchDataSource
) : SearchItemContract.Presenter {

    private val disposable = CompositeDisposable()

    private lateinit var view: SearchItemContract.View

    override fun attach(view: SearchItemContract.View) {
        this.view = view
    }

    override fun disposeRequests() {
        searchUserUseCase.clear()
    }

    override fun getUserByUsername(
        token: String,
        username: String
    ) {
        searchUserUseCase.initParams(token, username)
        searchUserUseCase.execute(object : DisposableSingleObserver<SearchModel<UserModel>>() {
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
            name = user.name,
            lastName = user.lastName,
            brand = user.brand,
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
}