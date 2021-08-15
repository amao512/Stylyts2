package kz.eztech.stylyts.search.presentation.shop.presenters

import io.reactivex.observers.DisposableSingleObserver
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.launch
import kz.eztech.stylyts.global.domain.models.clothes.ClothesBrandModel
import kz.eztech.stylyts.global.domain.models.clothes.ClothesModel
import kz.eztech.stylyts.global.domain.models.common.ResultsModel
import kz.eztech.stylyts.global.domain.usecases.clothes.GetClothesBrandsUseCase
import kz.eztech.stylyts.global.domain.usecases.clothes.GetClothesUseCase
import kz.eztech.stylyts.search.presentation.shop.contracts.ShopClothesListContract
import kz.eztech.stylyts.utils.Paginator
import javax.inject.Inject

/**
 * Created by Ruslan Erdenoff on 18.12.2020.
 */
class ShopClothesListPresenter @Inject constructor(
    private val paginator: Paginator.Store<ClothesModel>,
    private val brandsPaginator: Paginator.Store<ClothesBrandModel>,
    private val getClothesBrandsUseCase: GetClothesBrandsUseCase,
    private val getClothesUseCase: GetClothesUseCase
) : ShopClothesListContract.Presenter, CoroutineScope by CoroutineScope(Dispatchers.Main) {

    private lateinit var view: ShopClothesListContract.View

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

        launch {
            brandsPaginator.render = { view.renderPaginatorState(it) }
            brandsPaginator.sideEffects.consumeEach { effect ->
                when (effect) {
                    is Paginator.SideEffect.LoadPage -> loadBrandsPage(effect.currentPage)
                    is Paginator.SideEffect.ErrorEvent -> {}
                }
            }
        }
    }

    override fun disposeRequests() {
        getClothesBrandsUseCase.clear()
        getClothesUseCase.clear()
        cancel()
    }

    override fun attach(view: ShopClothesListContract.View) {
        this.view = view
    }

    override fun loadPage(page: Int) {
        getClothesUseCase.initParams(
            page = page,
            filterModel = view.getClothesFilter()
        )
        getClothesUseCase.execute(object : DisposableSingleObserver<ResultsModel<ClothesModel>>() {
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

    override fun getClothes() {
        paginator.proceed(Paginator.Action.Refresh)
    }

    override fun loadMorePage() {
        paginator.proceed(Paginator.Action.LoadMore)
    }

    override fun loadBrandsPage(page: Int) {
        getClothesBrandsUseCase.initParams(page = page)
        getClothesBrandsUseCase.execute(object : DisposableSingleObserver<ResultsModel<ClothesBrandModel>>() {
            override fun onSuccess(t: ResultsModel<ClothesBrandModel>) {
                brandsPaginator.proceed(Paginator.Action.NewPage(
                    pageNumber = t.page,
                    items = t.results
                ))
            }

            override fun onError(e: Throwable) {
                brandsPaginator.proceed(Paginator.Action.PageError(error = e))
            }
        })
    }

    override fun getClothesBrands() {
        brandsPaginator.proceed(Paginator.Action.Refresh)
    }

    override fun loadMoreBrands() {
        brandsPaginator.proceed(Paginator.Action.LoadMore)
    }
}