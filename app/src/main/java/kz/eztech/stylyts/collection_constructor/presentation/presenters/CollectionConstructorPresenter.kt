package kz.eztech.stylyts.collection_constructor.presentation.presenters

import io.reactivex.observers.DisposableSingleObserver
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.launch
import kz.eztech.stylyts.global.data.exception.ErrorHelper
import kz.eztech.stylyts.global.domain.models.clothes.ClothesModel
import kz.eztech.stylyts.global.domain.models.clothes.ClothesStyleModel
import kz.eztech.stylyts.global.domain.models.clothes.ClothesTypeModel
import kz.eztech.stylyts.global.domain.models.common.ResultsModel
import kz.eztech.stylyts.global.domain.usecases.clothes.GetClothesStylesUseCase
import kz.eztech.stylyts.global.domain.usecases.clothes.GetClothesTypesUseCase
import kz.eztech.stylyts.global.domain.usecases.clothes.GetClothesUseCase
import kz.eztech.stylyts.global.presentation.base.processViewAction
import kz.eztech.stylyts.collection_constructor.presentation.contracts.CollectionConstructorContract
import kz.eztech.stylyts.utils.Paginator
import javax.inject.Inject

/**
 * Created by Ruslan Erdenoff on 25.12.2020.
 */
class CollectionConstructorPresenter @Inject constructor(
    private val errorHelper: ErrorHelper,
    private val paginator: Paginator.Store<Any>,
    private val getClothesTypesUseCase: GetClothesTypesUseCase,
    private val getClothesUseCase: GetClothesUseCase,
    private val getClothesStylesUseCase: GetClothesStylesUseCase
) : CollectionConstructorContract.Presenter, CoroutineScope by CoroutineScope(Dispatchers.Main) {

    private lateinit var view: CollectionConstructorContract.View

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
        getClothesTypesUseCase.clear()
        getClothesUseCase.clear()
        getClothesStylesUseCase.clear()
        cancel()
    }

    override fun attach(view: CollectionConstructorContract.View) {
        this.view = view
    }

    override fun getTypes() {
        view.displayProgress()

        getClothesTypesUseCase.initParams()
        getClothesTypesUseCase.execute(object :
            DisposableSingleObserver<ResultsModel<ClothesTypeModel>>() {
            override fun onSuccess(t: ResultsModel<ClothesTypeModel>) {
                view.processViewAction {
                    processTypesResults(resultsModel = t)
                    hideProgress()
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

    override fun loadPage(page: Int) {
        if (view.isItems()) {
            loadClothes(page)
        }

        if (view.isStyles()) {
            loadStyles(page)
        }
    }

    override fun loadClothes(page: Int) {
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

    override fun loadStyles(page: Int) {
        getClothesStylesUseCase.initParams(page = page)
        getClothesStylesUseCase.execute(object :
            DisposableSingleObserver<ResultsModel<ClothesStyleModel>>() {
            override fun onSuccess(t: ResultsModel<ClothesStyleModel>) {
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

    override fun getClothesAndStyles() {
        view.displayProgress()
        paginator.proceed(Paginator.Action.Refresh)
    }
}