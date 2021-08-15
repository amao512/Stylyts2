package kz.eztech.stylyts.search.presentation.shop.presenters

import android.app.Application
import io.reactivex.observers.DisposableSingleObserver
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.launch
import kz.eztech.stylyts.global.domain.models.common.ResultsModel
import kz.eztech.stylyts.global.domain.models.user.UserModel
import kz.eztech.stylyts.global.domain.usecases.search.SearchProfileUseCase
import kz.eztech.stylyts.search.presentation.shop.contracts.ShopListContract
import kz.eztech.stylyts.search.presentation.shop.data.UIShopListData
import kz.eztech.stylyts.search.presentation.shop.data.models.ShopListItem
import kz.eztech.stylyts.utils.Paginator
import javax.inject.Inject

class ShopListPresenter @Inject constructor(
    private val paginator: Paginator.Store<ShopListItem>,
    private val searchProfileUseCase: SearchProfileUseCase,
    private val uiShopListData: UIShopListData,
    private val application: Application
) : ShopListContract.Presenter, CoroutineScope by CoroutineScope(Dispatchers.Main) {

    private lateinit var view: ShopListContract.View

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

    override fun attach(view: ShopListContract.View) {
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
                    items = uiShopListData.getShopList(
                        usersList = t.results,
                        currentUserId = view.getCurrendId()
                    )
                ))

                view.processCharacter(
                    character = uiShopListData.getCharacterList(list = t.results)
                )
            }

            override fun onError(e: Throwable) {
                paginator.proceed(Paginator.Action.PageError(error = e))
            }
        })
    }

    override fun getShops() {
        paginator.proceed(Paginator.Action.Refresh)
    }

    override fun loadMorePage() {
        paginator.proceed(Paginator.Action.LoadMore)
    }

    override fun getFilterList() {
        view.processFilterList(
            filterList = uiShopListData.getFilterList(context = application)
        )
    }
}