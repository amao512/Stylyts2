package kz.eztech.stylyts.presentation.presenters.collection_constructor

import io.reactivex.observers.DisposableSingleObserver
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.launch
import kz.eztech.stylyts.data.exception.ErrorHelper
import kz.eztech.stylyts.domain.models.clothes.*
import kz.eztech.stylyts.domain.models.common.ResultsModel
import kz.eztech.stylyts.domain.usecases.clothes.GetClothesBrandsUseCase
import kz.eztech.stylyts.domain.usecases.clothes.GetClothesCategoriesUseCase
import kz.eztech.stylyts.domain.usecases.clothes.GetClothesStylesUseCase
import kz.eztech.stylyts.domain.usecases.clothes.GetClothesTypesUseCase
import kz.eztech.stylyts.domain.usecases.wardrobe.CreateClothesByImageUseCase
import kz.eztech.stylyts.presentation.contracts.collection_constructor.SaveClothesAcceptContract
import kz.eztech.stylyts.presentation.dialogs.collection_constructor.SaveClothesAcceptDialog
import kz.eztech.stylyts.utils.Paginator
import javax.inject.Inject

class SaveClothesAcceptPresenter @Inject constructor(
    private val errorHelper: ErrorHelper,
    private val paginator: Paginator.Store<Any>,
    private val getClothesTypesUseCase: GetClothesTypesUseCase,
    private val getClothesCategoriesUseCase: GetClothesCategoriesUseCase,
    private val getClothesStylesUseCase: GetClothesStylesUseCase,
    private val getClothesBrandsUseCase: GetClothesBrandsUseCase,
    private val createClothesByImageUseCase: CreateClothesByImageUseCase
) : SaveClothesAcceptContract.Presenter, CoroutineScope by CoroutineScope(Dispatchers.Main) {

    private lateinit var view: SaveClothesAcceptContract.View

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
        getClothesCategoriesUseCase.clear()
        getClothesStylesUseCase.clear()
        getClothesBrandsUseCase.clear()
        createClothesByImageUseCase.clear()
        cancel()
    }

    override fun attach(view: SaveClothesAcceptContract.View) {
        this.view = view
    }

    override fun loadPage(page: Int) {
        when (view.getCurrentMode()) {
            SaveClothesAcceptDialog.TYPE_LIST_MODE -> loadTypes(page)
            SaveClothesAcceptDialog.CATEGORY_LIST_MODE -> loadCategories(page)
            SaveClothesAcceptDialog.STYLE_LIST_MODE -> loadStyles(page)
            SaveClothesAcceptDialog.BRAND_LIST_MODE -> loadBrands(page)
        }
    }

    override fun loadTypes(page: Int) {
        getClothesTypesUseCase.initParams(page = page)
        getClothesTypesUseCase.execute(object : DisposableSingleObserver<ResultsModel<ClothesTypeModel>>() {
            override fun onSuccess(t: ResultsModel<ClothesTypeModel>) {
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

    override fun loadCategories(page: Int) {
        getClothesCategoriesUseCase.initParams(
            typeId = view.getClothesCreateModel().clothesType,
            page = page
        )
        getClothesCategoriesUseCase.execute(object : DisposableSingleObserver<ResultsModel<ClothesCategoryModel>>() {
            override fun onSuccess(t: ResultsModel<ClothesCategoryModel>) {
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
        getClothesStylesUseCase.execute(object : DisposableSingleObserver<ResultsModel<ClothesStyleModel>>() {
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

    override fun loadBrands(page: Int) {
        getClothesBrandsUseCase.initParams(page = page)
        getClothesBrandsUseCase.execute(object : DisposableSingleObserver<ResultsModel<ClothesBrandModel>>() {
            override fun onSuccess(t: ResultsModel<ClothesBrandModel>) {
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

    override fun getList() {
        view.displaySmallProgress()
        paginator.proceed(Paginator.Action.Refresh)
    }

    override fun loadMorePage() {
        paginator.proceed(Paginator.Action.LoadMore)
    }

    override fun createClothes() {
        view.displayProgress()

        createClothesByImageUseCase.initParams(clothesCreateModel = view.getClothesCreateModel())
        createClothesByImageUseCase.execute(object : DisposableSingleObserver<ClothesModel>() {
            override fun onSuccess(t: ClothesModel) {
                view.processSuccessCreating(wardrobeModel = t)
                view.hideProgress()
            }

            override fun onError(e: Throwable) {
                view.hideProgress()
                view.displayMessage(msg = errorHelper.processError(e))
            }
        })
    }
}