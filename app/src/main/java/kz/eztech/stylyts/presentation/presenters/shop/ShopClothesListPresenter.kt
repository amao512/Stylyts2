package kz.eztech.stylyts.presentation.presenters.shop

import io.reactivex.observers.DisposableSingleObserver
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.launch
import kz.eztech.stylyts.data.exception.ErrorHelper
import kz.eztech.stylyts.domain.models.clothes.ClothesBrandModel
import kz.eztech.stylyts.domain.models.clothes.ClothesModel
import kz.eztech.stylyts.domain.models.common.ResultsModel
import kz.eztech.stylyts.domain.usecases.clothes.GetClothesBrandsUseCase
import kz.eztech.stylyts.domain.usecases.clothes.GetClothesUseCase
import kz.eztech.stylyts.presentation.base.processViewAction
import kz.eztech.stylyts.presentation.contracts.main.shop.ShopClothesListContract
import kz.eztech.stylyts.presentation.utils.Paginator
import javax.inject.Inject

/**
 * Created by Ruslan Erdenoff on 18.12.2020.
 */
class ShopClothesListPresenter @Inject constructor(
    private val errorHelper: ErrorHelper,
    private val paginator: Paginator.Store<Any>,
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
            token = view.getToken(),
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

    override fun getClothesBrands() {
        getClothesBrandsUseCase.initParams(token = view.getToken())
        getClothesBrandsUseCase.execute(object : DisposableSingleObserver<ResultsModel<ClothesBrandModel>>() {
            override fun onSuccess(t: ResultsModel<ClothesBrandModel>) {
                view.processViewAction {
                    processClothesBrands(resultsModel = t)
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