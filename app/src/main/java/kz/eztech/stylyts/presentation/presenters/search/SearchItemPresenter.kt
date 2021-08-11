package kz.eztech.stylyts.presentation.presenters.search

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.launch
import kz.eztech.stylyts.data.db.search.SearchDataSource
import kz.eztech.stylyts.data.db.search.UserSearchEntity
import kz.eztech.stylyts.data.exception.ErrorHelper
import kz.eztech.stylyts.domain.models.clothes.ClothesModel
import kz.eztech.stylyts.domain.models.common.ResultsModel
import kz.eztech.stylyts.domain.models.user.UserModel
import kz.eztech.stylyts.domain.usecases.search.SearchClothesUseCase
import kz.eztech.stylyts.domain.usecases.search.SearchProfileUseCase
import kz.eztech.stylyts.presentation.base.processViewAction
import kz.eztech.stylyts.presentation.contracts.search.SearchItemContract
import kz.eztech.stylyts.presentation.fragments.search.SearchItemFragment
import kz.eztech.stylyts.utils.Paginator
import javax.inject.Inject

/**
 * Created by Ruslan Erdenoff on 22.02.2021.
 */
class SearchItemPresenter @Inject constructor(
    private val errorHelper: ErrorHelper,
    private val paginator: Paginator.Store<Any>,
    private val searchProfileUseCase: SearchProfileUseCase,
    private val dataSource: SearchDataSource,
    private val searchClothesUseCase: SearchClothesUseCase
) : SearchItemContract.Presenter, CoroutineScope by CoroutineScope(Dispatchers.Main) {

    private val disposable = CompositeDisposable()

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

    private lateinit var view: SearchItemContract.View

    override fun attach(view: SearchItemContract.View) {
        this.view = view
    }

    override fun disposeRequests() {
        searchProfileUseCase.clear()
        searchClothesUseCase.clear()
        disposable.clear()
    }

    override fun loadPage(page: Int) {
        when (view.getCurrentPosition()) {
            SearchItemFragment.CLOTHES_POSITION -> searchClothes(page)
            SearchItemFragment.USERS_POSITION -> searchUser(page)
            SearchItemFragment.SHOPS_POSITION -> searchShop(page)
        }
    }

    override fun getList() {
        paginator.proceed(Paginator.Action.Refresh)
    }

    override fun loadMorePage() {
        paginator.proceed(Paginator.Action.LoadMore)
    }

    override fun searchUser(page: Int) {
        searchProfileUseCase.initParams(
            searchFilterModel = view.getSearchFilter().apply { isBrand = false },
            page = page
        )
        searchProfileUseCase.execute(object : DisposableSingleObserver<ResultsModel<UserModel>>() {
            override fun onSuccess(t: ResultsModel<UserModel>) {
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

    override fun deleteUserFromLocalDb(user: UserSearchEntity) {
        disposable.clear()
        disposable.add(
            dataSource.deleteUserSearch(user)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    getUserFromLocaleDb()
                }, {
                    view.displayMessage(msg = errorHelper.processError(it))
                })
        )
    }

    override fun searchShop(page: Int) {
        searchProfileUseCase.initParams(
            searchFilterModel = view.getSearchFilter().apply { isBrand = true },
            page = page
        )
        searchProfileUseCase.execute(object : DisposableSingleObserver<ResultsModel<UserModel>>() {
            override fun onSuccess(t: ResultsModel<UserModel>) {
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

    override fun searchClothes(page: Int) {
        searchClothesUseCase.initParams(
            searchFilterModel = view.getSearchFilter(),
            page = page
        )
        searchClothesUseCase.execute(object :
            DisposableSingleObserver<ResultsModel<ClothesModel>>() {
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
}