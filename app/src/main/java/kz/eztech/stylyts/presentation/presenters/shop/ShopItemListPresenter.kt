package kz.eztech.stylyts.presentation.presenters.shop

import io.reactivex.observers.DisposableSingleObserver
import kz.eztech.stylyts.data.exception.ErrorHelper
import kz.eztech.stylyts.domain.models.ResultsModel
import kz.eztech.stylyts.domain.models.clothes.ClothesCategoryModel
import kz.eztech.stylyts.domain.models.clothes.ClothesModel
import kz.eztech.stylyts.domain.models.filter.FilterModel
import kz.eztech.stylyts.domain.usecases.clothes.GetClothesCategoriesByTypeUseCase
import kz.eztech.stylyts.domain.usecases.clothes.GetClothesUseCase
import kz.eztech.stylyts.presentation.base.processViewAction
import kz.eztech.stylyts.presentation.contracts.main.shop.ShopItemListContract
import javax.inject.Inject

/**
 * Created by Asylzhan Seytbek on 08.04.2021.
 */
class ShopItemListPresenter @Inject constructor(
    private val errorHelper: ErrorHelper,
    private val getClothesUseCase: GetClothesUseCase,
    private val getClothesCategoriesByTypeUseCase: GetClothesCategoriesByTypeUseCase
) : ShopItemListContract.Presenter {

    private lateinit var view: ShopItemListContract.View

    override fun disposeRequests() {
        getClothesUseCase.clear()
        getClothesCategoriesByTypeUseCase.clear()
    }

    override fun attach(view: ShopItemListContract.View) {
        this.view = view
    }

    override fun getCategoriesByType(token: String, clothesTypeId: Int) {
        view.displayProgress()

        getClothesCategoriesByTypeUseCase.initParams(token, clothesTypeId)
        getClothesCategoriesByTypeUseCase.execute(object : DisposableSingleObserver<ResultsModel<ClothesCategoryModel>>() {
            override fun onSuccess(t: ResultsModel<ClothesCategoryModel>) {
                view.processViewAction {
                    processCategories(resultsModel = t)
                    hideProgress()
                }
            }

            override fun onError(e: Throwable) {
                view.processViewAction {
                    displayMessage(msg = errorHelper.processError(e))
                    hideProgress()
                }
            }
        })
    }

    override fun getClothesResultsByType(
        token: String,
        filterModel: FilterModel
    ) {
        getClothesUseCase.initParams(
            token = token,
            filterModel = filterModel
        )
        getClothesUseCase.execute(object : DisposableSingleObserver<ResultsModel<ClothesModel>>() {
            override fun onSuccess(t: ResultsModel<ClothesModel>) {
                view.processViewAction {
                    processClothesResults(resultsModel = t)
                }
            }

            override fun onError(e: Throwable) {
                view.processViewAction {
                    displayMessage(msg = errorHelper.processError(e))
                }
            }
        })
    }

    override fun getClothesResultsByCategory(
        token: String,
        filterModel: FilterModel
    ) {
        getClothesUseCase.initParams(
            token = token,
            filterModel = filterModel
        )
        getClothesUseCase.execute(object : DisposableSingleObserver<ResultsModel<ClothesModel>>() {
            override fun onSuccess(t: ResultsModel<ClothesModel>) {
                view.processClothesResults(resultsModel = t)
            }

            override fun onError(e: Throwable) {
                view.processViewAction {
                    displayMessage(msg = errorHelper.processError(e))
                }
            }
        })
    }
}