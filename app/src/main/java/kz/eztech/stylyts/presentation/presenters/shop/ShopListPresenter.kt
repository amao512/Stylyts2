package kz.eztech.stylyts.presentation.presenters.shop

import io.reactivex.observers.DisposableSingleObserver
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.launch
import kz.eztech.stylyts.data.exception.ErrorHelper
import kz.eztech.stylyts.domain.models.common.ResultsModel
import kz.eztech.stylyts.domain.models.shop.ShopListItem
import kz.eztech.stylyts.domain.models.user.UserModel
import kz.eztech.stylyts.domain.usecases.search.SearchProfileUseCase
import kz.eztech.stylyts.presentation.contracts.shop.ShopListContract
import kz.eztech.stylyts.presentation.utils.Paginator
import java.util.*
import javax.inject.Inject

class ShopListPresenter @Inject constructor(
    private val paginator: Paginator.Store<ShopListItem>,
    private val searchProfileUseCase: SearchProfileUseCase
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
            token = view.getToken(),
            searchFilterModel = view.getSearchFilter(),
            page = page
        )
        searchProfileUseCase.execute(object : DisposableSingleObserver<ResultsModel<UserModel>>() {
            override fun onSuccess(t: ResultsModel<UserModel>) {
                val preparedResults: MutableList<ShopListItem> = mutableListOf()
                val characterList: MutableList<String> = mutableListOf()

                sortedShopList(
                    results = t.results.filter { it.id != view.getCurrendId() }
                ).map { list ->
                    list.map {
                        preparedResults.add(it)
                    }

                    characterList.add(list[0].item as String)
                }

                paginator.proceed(Paginator.Action.NewPage(
                    pageNumber = t.page,
                    items = preparedResults
                ))
                view.processCharacter(character = characterList)
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

    private fun sortedShopList(results: List<UserModel>): List<List<ShopListItem>> {
        val list: MutableList<MutableList<ShopListItem>> = mutableListOf()

        results.map { shop ->
            var item: ShopListItem?
            var position: Int = -1
            var counter = 0

            list.map { filterList ->
                item = filterList.find {
                    it.item == shop.username.substring(0, 1)
                        .toUpperCase(Locale.getDefault())
                }

                if (item != null) {
                    position = counter
                }

                counter++
            }

            if (position != -1) {
                list[position].add(
                    ShopListItem(
                        id = shop.id,
                        item = shop
                    )
                )
            } else {
                val character = ShopListItem(
                    id = 0,
                    item = shop.username.substring(0, 1).toUpperCase(Locale.getDefault())
                )

                val newBrand = ShopListItem(
                    id = shop.id,
                    item = shop
                )

                list.add(mutableListOf(character, newBrand))
            }
        }

        list.sortBy { (it[0].item as String) }

        return list
    }
}