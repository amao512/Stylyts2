package kz.eztech.stylyts.presentation.presenters.search

import io.reactivex.observers.DisposableSingleObserver
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.launch
import kz.eztech.stylyts.domain.models.common.ResultsModel
import kz.eztech.stylyts.domain.models.user.UserModel
import kz.eztech.stylyts.domain.usecases.search.SearchProfileUseCase
import kz.eztech.stylyts.presentation.contracts.profile.UserSearchContract
import kz.eztech.stylyts.presentation.utils.Paginator
import javax.inject.Inject

class UserSearchPresenter @Inject constructor(
    private val paginator: Paginator.Store<UserModel>,
    private val searchProfileUseCase: SearchProfileUseCase,
) : UserSearchContract.Presenter, CoroutineScope by CoroutineScope(Dispatchers.Main) {

    private lateinit var view: UserSearchContract.View

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
        searchProfileUseCase.clear()
        cancel()
    }

    override fun attach(view: UserSearchContract.View) {
        this.view = view
    }

    override fun loadPage(page: Int) {
        searchProfileUseCase.initParams(
            searchFilterModel = view.getSearchFilter(),
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

    override fun loadMorePage() {
        paginator.proceed(Paginator.Action.LoadMore)
    }

    override fun getUsers() {
        paginator.proceed(Paginator.Action.Refresh)
    }
}